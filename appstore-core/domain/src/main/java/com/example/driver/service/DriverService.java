package com.example.driver.service;

import static com.example.delivery.exception.DeliveryErrorCode.DELIVERY_NOT_FOUND;
import static com.example.driver.exception.DriverErrorCode.DRIVER_DUPLICATE_NAME;
import static com.example.driver.exception.DriverErrorCode.DRIVER_NOT_FOUND;

import com.example.completeddelivery.entity.CompletedDelivery;
import com.example.completeddelivery.event.CompletedDeliverySavedEvent;
import com.example.completeddelivery.feign.CompletedDeliveryClient;
import com.example.delivery.event.DeliveryBulkDeletedEvent;
import com.example.delivery.exception.DeliveryException;
import com.example.delivery.model.DeliveryAddress;
import com.example.delivery.model.dto.response.DeliveryDetailResponse;
import com.example.delivery.model.entity.Delivery;
import com.example.delivery.repository.DeliveryRepository;
import com.example.driver.exception.DriverException;
import com.example.driver.model.dto.request.CreateDriverRequest;
import com.example.driver.model.dto.request.SignInDriverRequest;
import com.example.driver.model.entity.Driver;
import com.example.driver.repository.DriverRepository;
import com.example.global.aop.filter.FilterByDeletedStatus;
import com.example.global.jwt.CustomUserDetails;
import com.example.global.jwt.JwtToken;
import com.example.global.jwt.JwtTokenProvider;
import com.example.s3.service.S3Service;
import com.example.stopover.entity.StopOver;
import com.example.stopover.repository.StopOverRepository;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository driverRepository;
    private final DeliveryRepository deliveryRepository;
    private final StopOverRepository stopOverRepository;
    private final S3Service s3Service;
    private final ApplicationEventPublisher eventPublisher;
    private final CompletedDeliveryClient completedDeliveryClient;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void createDriver(CreateDriverRequest createDriverRequest) throws IOException {
        validateDuplicateDriverName(createDriverRequest.getDriverName());

        Driver driver = createDriverRequest.toDriver();
        driver.updatePassword(passwordEncoder.encode(driver.getPassword()));
        driver.assignDriverRole();

        String imageUrl = s3Service.uploadFile(createDriverRequest.getMultipartFile());
        driver.updateImageUrl(imageUrl);

        try {
            driverRepository.save(driver);
        } catch (DataIntegrityViolationException e) {
            throw new DriverException(DRIVER_DUPLICATE_NAME);
        }
    }

    public JwtToken signIn(SignInDriverRequest signInDriverRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(signInDriverRequest.getDriverName(), signInDriverRequest.getPassword());

        // 2. 실제 검증. authenticate() 메서드를 통해 요청된 Member 에 대한 검증 진행
        // authenticate 메서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드 실행
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (InternalAuthenticationServiceException e) {
            throw new RuntimeException(e.getMessage());
        }
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = customUserDetails.getId();
        return jwtTokenProvider.generateToken(authentication, userId);
    }

    @Transactional
    public void deleteDriver(Long driverId) {
        Driver driver = driverRepository.findById(driverId)
            .orElseThrow(() -> DriverException.fromErrorCode(DRIVER_NOT_FOUND));

        driverRepository.delete(driver);
    }

    @FilterByDeletedStatus("F")
    @Transactional
    public DeliveryDetailResponse matchingDeliveryWithDriver(Long driverId, String reservationNumber) {
        validatedDriverExist(driverId);

        Delivery delivery = deliveryRepository.findByReservationNumberWithPLock(reservationNumber)
            .orElseThrow(() -> new DeliveryException(DELIVERY_NOT_FOUND));

        List<StopOver> stopOvers = stopOverRepository.findStopOverByDeliveryId(delivery.getId());

        String matchIdempotencyKey = delivery.generateMatchIdempotencyKey(driverId);
        if (deliveryRepository.existsByIdempotencyKey(matchIdempotencyKey)) {
            return getDeliveryDetailResponse(stopOvers, delivery);
        }

        delivery.updateIdempotencyKey(matchIdempotencyKey);
        delivery.matchDriver(driverId);
        delivery.updateStatusToAssignmentCompleted();

        return getDeliveryDetailResponse(stopOvers, delivery);
    }

    @Transactional
    public DeliveryDetailResponse completedDelivery(String reservationNumber) {
        Delivery delivery = deliveryRepository.findByReservationNumber(reservationNumber)
            .orElseThrow(() -> new DeliveryException(DELIVERY_NOT_FOUND));

        List<StopOver> stopOvers = stopOverRepository.findStopOverByDeliveryId(delivery.getId());

        delivery.updateStatusToDeliveryCompleted();

        CompletedDelivery completedDelivery = delivery.toCompletedDelivery(delivery);
        eventPublisher.publishEvent(new CompletedDeliverySavedEvent(completedDelivery));
        eventPublisher.publishEvent(new DeliveryBulkDeletedEvent(reservationNumber));

        completedDeliveryClient.saveCompletedDelivery(completedDelivery);

        return getDeliveryDetailResponse(stopOvers, delivery);
    }

    private void validatedDriverExist(Long driverId) {
        driverRepository.findById(driverId)
            .orElseThrow(() -> new DriverException(DRIVER_NOT_FOUND));
    }

    private void validateDuplicateDriverName(String driverName) {
        final boolean isDuplicate = driverRepository.findByDriverName(driverName).isPresent();
        if (isDuplicate) {
            throw DriverException.fromErrorCode(DRIVER_DUPLICATE_NAME);
        }
    }

    private DeliveryDetailResponse getDeliveryDetailResponse(List<StopOver> stopOvers, Delivery delivery) {
        if (CollectionUtils.isEmpty(stopOvers)) {
            return DeliveryDetailResponse.from(delivery, Collections.emptyList());
        }

        return DeliveryDetailResponse.from(delivery, extractDeliveryAddresses(stopOvers));
    }

    private List<DeliveryAddress> extractDeliveryAddresses(List<StopOver> stopOvers) {
        return stopOvers.stream()
            .map(StopOver::getDeliveryAddress)
            .toList();
    }

}
