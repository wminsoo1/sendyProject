package com.example.driver.service;

import static com.example.delivery.service.DeliveryDummyGenerator.createDeliverySaveRequest;
import static com.example.driver.service.DriverDummyGenerator.createDriverRequest;
import static com.example.driver.service.DriverDummyGenerator.createDummyDriver;
import static com.example.driver.service.DriverDummyGenerator.createDummyVehicle;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.delivery.model.dto.request.DeliverySaveRequest;
import com.example.delivery.model.dto.response.DeliveryDetailResponse;
import com.example.delivery.model.dto.response.DeliverySaveResponse;
import com.example.delivery.repository.DeliveryRepository;
import com.example.delivery.service.DeliveryService;
import com.example.driver.model.dto.request.CreateDriverRequest;
import com.example.driver.model.entity.Driver;
import com.example.driver.repository.DriverRepository;
import com.example.TestConfiguration;
import com.example.global.jwt.CustomUserDetails;
import com.example.member.model.dto.request.SignUpRequest;
import com.example.member.repository.MemberRepository;
import com.example.member.service.MemberService;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutorService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {TestConfiguration.class})
@SpringBootTest
@EnableFeignClients(basePackages = "com.example.global.navermap") // 추가
public class DriverIntegrationTest {

    @Autowired
    DriverService driverService;

    @Autowired
    DeliveryService deliveryService;

    @Autowired
    MemberService memberService;

    @Autowired
    DriverRepository driverRepository;

    @Autowired
    DeliveryRepository deliveryRepository;

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("동시성 테스트")
    void test1() {
        // 드라이버 및 배달 생성
        Long memberId = 1L;
        Long deliveryId = 1L;

        CreateDriverRequest createDriverRequest = createDriverRequest("운전자", "1234567a", createDummyVehicle());
        DeliverySaveRequest deliverySaveRequest = createDeliverySaveRequest();

        Driver driver = createDummyDriver();
        CustomUserDetails customUserDetails = createCustomUserDetails(driver);
        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(customUserDetails, customUserDetails.getPassword(), customUserDetails.getAuthorities())
        );

        SignUpRequest signUpRequest = SignUpRequest.builder()
            .memberName("member")
            .email("asd@naver.com")
            .password("1234567a!")
            .build();

        memberService.signUp(signUpRequest);
        driverService.createDriver(createDriverRequest);
        DeliverySaveResponse deliverySaveResponse = deliveryService.saveDelivery(deliverySaveRequest, memberId);

        String reservationNumber = deliverySaveResponse.getReservationNumber();

        assertTrue(driverRepository.findById(driver.getId()).isPresent(), "드라이버가 존재해야 합니다.");
        assertTrue(deliveryRepository.findById(deliveryId).isPresent(), "배달이 존재해야 합니다.");

        int numOfThread = 5;
        ExecutorService service = new DelegatingSecurityContextExecutorService(Executors.newFixedThreadPool(numOfThread));

        for (int i = 0; i < numOfThread; i++) {
            service.submit(() -> {
                try {
                    DeliveryDetailResponse deliveryDetailResponseResponseEntity = driverService.matchingDeliveryWithDriver(customUserDetails.getId(), reservationNumber);
                    System.out.println("deliveryDetailResponseResponseEntity = "
                        + deliveryDetailResponseResponseEntity);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        service.shutdown();
    }

    private CustomUserDetails createCustomUserDetails(Driver driver) {
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("DRIVER"));
        User user = new User(driver.getUsername(), driver.getPassword(), authorities);
        CustomUserDetails customUserDetails = new CustomUserDetails(user, driver.getId());
        return customUserDetails;
    }

}
