package com.example.driver.service;

import static com.example.driver.service.DriverDummyGenerator.creatDummySignInDriverRequest;
import static com.example.driver.service.DriverDummyGenerator.createDriverRequest;
import static com.example.driver.service.DriverDummyGenerator.createDummyVehicle;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.delivery.repository.DeliveryRepository;
import com.example.stopover.repository.StopOverRepository;
import com.example.driver.exception.DriverException;
import com.example.driver.model.dto.request.CreateDriverRequest;
import com.example.driver.model.dto.request.SignInDriverRequest;
import com.example.driver.model.entity.Driver;
import com.example.driver.repository.DriverRepository;
import com.example.global.jwt.CustomUserDetails;
import com.example.global.jwt.JwtTokenProvider;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class DriverServiceTest {

    @InjectMocks
    DriverService driverService;

    @Mock
    DriverRepository driverRepository;

    @Mock
    DeliveryRepository deliveryRepository;

    @Mock
    StopOverRepository stopOverRepository;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    JwtTokenProvider jwtTokenProvider;

    @Mock
    PasswordEncoder passwordEncoder;


    @Nested class createDriver {

        @DisplayName("운전자 이름이 중복되면 DeliveryException을 throw한다.")
        @Test
        void test1() {
            //given
            CreateDriverRequest createDriverRequest = createDriverRequest("운전자", "1234567a", createDummyVehicle());
            Driver driver = Driver
                .builder()
                .build();
            
            //when
            doReturn(Optional.of(driver)).when(driverRepository)
                .findByDriverName(createDriverRequest.getDriverName());

            //then
            assertThrows(DriverException.class, () -> driverService.createDriver(createDriverRequest));
        }

        @DisplayName("데이터베이스에서 예약 번호 중복 시 DeliveryException을 throw한다.")
        @Test
        void test2() {
            //given
            CreateDriverRequest createDriverRequest = createDriverRequest("운전자", "1234567a", createDummyVehicle());

            //when
            doThrow(DataIntegrityViolationException.class).when(driverRepository)
                .save(any(Driver.class));

            //then
            assertThrows(DriverException.class, () -> driverService.createDriver(createDriverRequest));
        }

        @DisplayName("운전자 생성 성공")
        @Test
        void test1000() {
            //given
            CreateDriverRequest createDriverRequest = createDriverRequest("운전자", "1234567a", createDummyVehicle());

            when(driverRepository.findByDriverName(createDriverRequest.getDriverName()))
                .thenReturn(Optional.empty());

            //when
            driverService.createDriver(createDriverRequest);

            //then
            verify(driverRepository).findByDriverName(createDriverRequest.getDriverName());
            verify(driverRepository).save(any(Driver.class));
        }
    }

    @Nested class signIn {

        @DisplayName("사용자 정보가 데이터베이스에 없을 때")
        @Test
        void test1() {
            //given
            SignInDriverRequest signInDriverRequest = creatDummySignInDriverRequest();
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(signInDriverRequest.getDriverName(), signInDriverRequest.getPassword());

            //when
            doReturn(null).when(authenticationManager)
                .authenticate(authenticationToken);

            //then
            assertThrows(RuntimeException.class, () -> driverService.signIn(signInDriverRequest));
        }

        @DisplayName("로그인 성공 - jwt 토큰 반환")
        @Test
        void test1000() {
            //given
            SignInDriverRequest signInDriverRequest = creatDummySignInDriverRequest();
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(signInDriverRequest.getDriverName(), signInDriverRequest.getPassword());
            User user = (User) User.builder()
                .username("사용자")
                .password("1234567a")
                .build();
            Authentication authentication = Mockito.mock(Authentication.class);
            CustomUserDetails customUserDetails = new CustomUserDetails(user, 1L);

            doReturn(authentication).when(authenticationManager)
                .authenticate(authenticationToken);
            doReturn(customUserDetails).when(authentication)
                .getPrincipal();

            //when
            driverService.signIn(signInDriverRequest);

            //then
            verify(authenticationManager).authenticate(any(Authentication.class));
            verify(jwtTokenProvider).generateToken(any(Authentication.class), any(Long.class));
        }
    }
}