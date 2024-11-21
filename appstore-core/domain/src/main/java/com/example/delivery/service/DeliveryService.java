package com.example.delivery.service;

import static com.example.deleteddelivery.exception.DeletedDeliveryErrorCode.*;
import static com.example.delivery.exception.DeliveryErrorCode.DELIVERY_NOT_FOUND;
import static com.example.delivery.exception.DeliveryErrorCode.MEMBER_ID_RESERVATION_NUMBER_MISMATCH;
import static com.example.delivery.exception.DeliveryErrorCode.RESERVATION_NUMBER_DUPLICATED;
import static com.example.delivery.exception.DeliveryErrorCode.RESERVATION_NUMBER_NOT_FOUND;
import static com.example.driver.exception.DriverErrorCode.DRIVER_NOT_FOUND;

import com.example.deleteddelivery.exception.DeletedDeliveryErrorCode;
import com.example.deleteddelivery.exception.DeletedDeliveryException;
import com.example.delivery.exception.DeliveryException;
import com.example.delivery.model.DeliveryAddress;
import com.example.delivery.model.DropLocation;
import com.example.delivery.model.PickUpLocation;
import com.example.delivery.model.dto.request.DeliverySaveRequest;
import com.example.delivery.model.dto.request.DeliveryUpdateRequest;
import com.example.delivery.model.dto.response.DeliveryDetailResponse;
import com.example.delivery.model.dto.response.DeliverySaveResponse;
import com.example.delivery.model.dto.response.DeliverySummaryResponse;
import com.example.delivery.model.entity.Delivery;
import com.example.delivery.repository.DeliveryRepository;
import com.example.delivery.utils.ReservationNumberGenerator;
import com.example.global.aop.filter.FilterByDeletedStatus;
import com.example.global.navermap.NaverMapGeocode;
import com.example.global.navermap.dto.LocationResult;
import com.example.member.event.MemberExistenceCheckEvent;
import com.example.stopover.entity.StopOver;
import com.example.stopover.event.StopOverBulkDeletedEvent;
import com.example.stopover.event.StopOverDeletedEvent;
import com.example.stopover.event.StopOverSavedEvent;
import com.example.stopover.event.StopOverUpdatedEvent;
import com.example.stopover.repository.StopOverRepository;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final StopOverRepository stopOverRepository;
    private final NaverMapGeocode naverMapGeocode;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional // 데이터베이스에 unique 설정 + 체크 로직으로 동시성 문제 터지면 그냥 오류 발생 시키키 -> 저장할 때 동시성 문제는 거의 안 생기기 때문
    public DeliverySaveResponse saveDelivery(DeliverySaveRequest deliverySaveRequest, Long memberId) { //나중에 결제 추가하면 배달 상태도 추가해줘야 함
        validatedMemberExist(memberId);

        final Delivery delivery = deliverySaveRequest.toDelivery();
        delivery.updateMember(memberId);
        delivery.updateDeliveryFee(BigDecimal.valueOf(10000)); //결제금액은 프론트에서 결정
        delivery.updateStatusToPaymentPending();

        updateDeliveryAddressAsync(deliverySaveRequest.getDeliveryAddress(), delivery);

        String reservationNumber = ReservationNumberGenerator.withDate(deliveryRepository.count());
        validatedDuplicateReservationNumber(reservationNumber);

        delivery.updateReservationNumber(reservationNumber);

        try {
            deliveryRepository.save(delivery);
        } catch (DataIntegrityViolationException e) { //unique 에러
            throw new DeliveryException(RESERVATION_NUMBER_DUPLICATED);
        }

        eventPublisher.publishEvent(new StopOverSavedEvent(delivery, deliverySaveRequest));

        return DeliverySaveResponse.from(delivery, deliverySaveRequest.getStopOverAddresses());
    }

    @CacheEvict(value = "deliveryDetails", key = "#p0", cacheManager = "cacheManager")
    @Transactional
    public void deleteDelivery(String reservationNumber) {
        final Delivery delivery = deliveryRepository.findByReservationNumber(reservationNumber)
                .orElseThrow(() -> DeliveryException.fromErrorCode(DELIVERY_NOT_FOUND));

        eventPublisher.publishEvent(new StopOverDeletedEvent(reservationNumber));
        deliveryRepository.delete(delivery);
    }

    @Transactional
    public void deleteDeliveryInBulk(String reservationNumber) {
        final Delivery delivery = deliveryRepository.findByReservationNumber(reservationNumber)
            .orElseThrow(() -> DeliveryException.fromErrorCode(DELIVERY_NOT_FOUND));

        eventPublisher.publishEvent(new StopOverBulkDeletedEvent(reservationNumber));
        deliveryRepository.deleteInBulk(delivery);
    }

    @Transactional
    public void deleteDeliveriesInBulk(List<Delivery> deliveries) {
        if (CollectionUtils.isEmpty(deliveries)) {
            return;
        }

        List<Long> deliveriesId = deliveries.stream()
            .map(Delivery::getId)
            .toList();

        stopOverRepository.deleteAllByDeliveriesInBulk(deliveriesId);
        deliveryRepository.deleteAllInBulk(deliveries);
    }


    @CachePut(value = "deliveryDetails", key = "#p0", cacheManager = "cacheManager")
    @Transactional
    public DeliveryDetailResponse updateDelivery(String reservationNumber, DeliveryUpdateRequest deliveryUpdateRequest) { //경유지 변경감지 봐야함
        final Delivery delivery = deliveryRepository.findByReservationNumber(reservationNumber)
                .orElseThrow(() -> DeliveryException.fromErrorCode(DELIVERY_NOT_FOUND));

        delivery.update(deliveryUpdateRequest);
        updateDeliveryAddressAsync(deliveryUpdateRequest.getDeliveryAddress(), delivery);

        if (deliveryUpdateRequest.getDeliveryAddress() != null) {
            eventPublisher.publishEvent(new StopOverUpdatedEvent(delivery, deliveryUpdateRequest.getStopOverAddresses()));
        }

        return DeliveryDetailResponse.from(delivery, deliveryUpdateRequest.getStopOverAddresses());
    }

    @FilterByDeletedStatus("F")
    @Cacheable(value = "deliveryDetails", key = "#p0", cacheManager = "cacheManager")
    public DeliveryDetailResponse getDeliveryDetail(String reservationNumber, Long memberId) {
        validatedMemberExist(memberId);
        validatedReservationNumberExist(reservationNumber);

        final Delivery delivery = deliveryRepository.findDeliveryByReservationNumberAndMemberId(reservationNumber, memberId)
                .orElseThrow(() -> DeliveryException.fromErrorCode(MEMBER_ID_RESERVATION_NUMBER_MISMATCH));

        List<StopOver> stopOvers = stopOverRepository.findStopOverByDeliveryId(delivery.getId());

        return getDeliveryDetailResponse(stopOvers, delivery);
    }

    @FilterByDeletedStatus("F")
    public DeliveryDetailResponse getDeliveryDetailForDriver(String reservationNumber) {
        validatedReservationNumberExist(reservationNumber);

        Delivery delivery = deliveryRepository.findByReservationNumber(reservationNumber)
                .orElseThrow(() -> new DeliveryException(DRIVER_NOT_FOUND));

        List<StopOver> stopOvers = stopOverRepository.findStopOverByDeliveryId(delivery.getId());

        return getDeliveryDetailResponse(stopOvers, delivery);
    }

    @FilterByDeletedStatus("F")
    public List<DeliverySummaryResponse> getDeliverySummaryResponseForMember(Long memberId, Long deliveryId, int pageSize) {
        return deliveryRepository.getDeliverySummaryResponseForMember(memberId, deliveryId, pageSize);
    }

    @FilterByDeletedStatus("F")
    public List<DeliverySummaryResponse> getDeliverySummaryResponseForDriver(Long driverId, Long deliveryId, Double driverLatitude, Double driverLongitude, int pageSize) {
        return deliveryRepository.getDeliverySummaryResponseForDriver(driverId,deliveryId, driverLatitude, driverLongitude, pageSize);
    }

    /*
    public List<DeliverySummaryResponse> getDeliverySummariesWithCache (Long memberId) {
        validatedMemberExisted(memberId);

        List<DeliverySummaryResponse> completedDeliveriesSummary = deliveryCacheService.getCompletedDeliveriesSummary(memberId).getDeliverySummaryResponses();
        List<DeliverySummaryResponse> notCompletedDeliveriesSummary = getNotCompletedDeliveriesSummary(memberId);

        return Stream.concat(completedDeliveriesSummary.stream(), notCompletedDeliveriesSummary.stream())
                .toList();
    }

    public List<DeliverySummaryResponse> getNotCompletedDeliveriesSummary(Long memberId) {
        validatedMemberExisted(memberId);

        List<Delivery> deliveries = deliveryRepository.findNotCompletedDeliveriesByMemberId(memberId,   DELIVERY_COMPLETED);
        if (deliveries == null) {
            throw new DeliveryException(DRIVER_NOT_FOUND);
        }

        final List<Long> deliveryIds = deliveries.stream()
                .map(Delivery::getId)
                .toList();

        return createDeliverySummaryResponses(deliveryIds, deliveries);
    }

    private List<DeliverySummaryResponse> createDeliverySummaryResponses(List<Long> deliveryIds, List<Delivery> deliveries) {
    final List<StopOver> stopOverByDeliveryIds = stopOverRepository.findStopOverByDeliveryIds(deliveryIds);
    if (stopOverByDeliveryIds == null || stopOverByDeliveryIds.isEmpty()) {
        return deliveries.stream()
                .map(delivery -> DeliverySummaryResponse.from(delivery, 0))
                .toList();
    }

    final Map<Long, List<StopOver>> stopOversByDeliveryId = stopOverByDeliveryIds.stream()
            .collect(Collectors.groupingBy(stopOver -> stopOver.getDelivery().getId()));

    return deliveries.stream()
            .map(delivery -> {
                int size = stopOversByDeliveryId.getOrDefault(delivery.getId(), Collections.emptyList()).size();
                return DeliverySummaryResponse.from(delivery, size);
            })
            .toList();
    }

    */

    private DeliveryDetailResponse getDeliveryDetailResponse(List<StopOver> stopOvers, Delivery delivery) {
        if (stopOvers == null || stopOvers.isEmpty()) {
            return DeliveryDetailResponse.from(delivery, Collections.emptyList());
        }

        return DeliveryDetailResponse.from(delivery, extractDeliveryAddresses(stopOvers));
    }

    private List<DeliveryAddress> extractDeliveryAddresses(List<StopOver> stopOvers) {
        return stopOvers.stream()
            .map(StopOver::getDeliveryAddress)
            .toList();
    }

    private void validatedDuplicateReservationNumber(String reservationNumber) {
        if (deliveryRepository.existsByReservationNumber(reservationNumber)) {
            throw new DeliveryException(RESERVATION_NUMBER_DUPLICATED);
        }
    }

    private void validatedMemberExist(Long memberId) {
        eventPublisher.publishEvent(new MemberExistenceCheckEvent(memberId));
    }

    private void updateDeliveryAddress(DeliverySaveRequest deliverySaveRequest, Delivery delivery) {
        String pickUpAddress = deliverySaveRequest.getDeliveryAddress().getPickupLocation().getPickUpAddress();
        String dropAddress = deliverySaveRequest.getDeliveryAddress().getDropLocation().getDropAddress();

        PickUpLocation pickUpLocation = naverMapGeocode.getPickUpLocation(pickUpAddress);
        DropLocation dropLocation = naverMapGeocode.getDropLocation(dropAddress);

        delivery.updateDeliveryAddress(new DeliveryAddress(pickUpLocation, dropLocation));
    }

    private void updateDeliveryAddressAsync(DeliveryAddress deliveryAddress, Delivery delivery) {
        String pickUpAddress = deliveryAddress.getPickupLocation().getPickUpAddress();
        String dropAddress = deliveryAddress.getDropLocation().getDropAddress();

        CompletableFuture<LocationResult> locationResultFuture = naverMapGeocode.getLocationResultFuture(pickUpAddress, dropAddress);
        LocationResult locationResult;
        try {
            locationResult = locationResultFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        PickUpLocation pickUpLocation = (PickUpLocation) naverMapGeocode.getLocation(locationResult.getPickUpResult(), pickUpAddress, PickUpLocation::create);
        DropLocation dropLocation = (DropLocation) naverMapGeocode.getLocation(locationResult.getDropResult(), dropAddress, DropLocation::create);
        delivery.updateDeliveryAddress(new DeliveryAddress(pickUpLocation, dropLocation));
    }

    private void validatedReservationNumberExist(String reservationNumber) {
        final boolean isReservationNumberExist = deliveryRepository.existsByReservationNumber(reservationNumber);
        if (!isReservationNumberExist) {
            throw DeliveryException.fromErrorCode(RESERVATION_NUMBER_NOT_FOUND);
        }
    }

}
