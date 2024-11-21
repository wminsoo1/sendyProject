//package com.example.delivery.service;
//
//import static com.example.delivery.service.DeliveryDummyGenerator.*;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.doThrow;
//import static org.mockito.Mockito.lenient;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import com.example.delivery.exception.DeliveryException;
//import com.example.delivery.model.dto.request.DeliverySaveRequest;
//import com.example.delivery.model.dto.request.DeliveryUpdateRequest;
//import com.example.delivery.model.dto.response.DeliveryDetailResponse;
//import com.example.delivery.model.entity.Delivery;
//import com.example.delivery.repository.DeliveryRepository;
//import com.example.global.navermap.dto.LocationResult;
//import com.example.stopover.repository.StopOverRepository;
//import com.example.global.navermap.NaverMapGeocode;
//import com.example.member.event.MemberExistenceCheckEvent;
//import com.example.member.exception.MemberException;
//import com.example.stopover.event.StopOverDeletedEvent;
//import com.example.stopover.event.StopOverSavedEvent;
//import com.example.stopover.event.StopOverUpdatedEvent;
//import java.util.Optional;
//import java.util.concurrent.CompletableFuture;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.context.ApplicationEventPublisher;
//
//@ExtendWith(MockitoExtension.class)
//class DeliveryRepositoryTest {
//
//    @InjectMocks
//    com.example.delivery.service.DeliveryService deliveryService;
//
//    @Mock
//    StopOverRepository stopOverRepository;
//
//    @Mock
//    ApplicationEventPublisher eventPublisher;
//
//    @Mock
//    NaverMapGeocode naverMapGeocode;
//
//    @Mock
//    DeliveryRepository deliveryRepository;
//
//    @Nested
//    class saveDelivery {
//
//        @DisplayName("memberId를 갖는 entity를 찾지 못하면, MemberException을 throw한다.")
//        @Test
//        void test1() {
//            //given
//            Long invalidMemberId = 100L;
//            DeliverySaveRequest deliverySaveRequest = createDeliverySaveRequest();
//
//            //when
//            doThrow(MemberException.class).when(eventPublisher)
//                .publishEvent(any(MemberExistenceCheckEvent.class));
//
//            //then
//            assertThrows(MemberException.class,
//                () -> deliveryService.saveDelivery(deliverySaveRequest, invalidMemberId)
//            );
//        }
//
//        @DisplayName("예약 번호가 중복되면 DeliveryException을 throw한다.")
//        @Test
//        void test2() {
//            //given
//            Long invalidMemberId = 100L;
//            DeliverySaveRequest deliverySaveRequest = createDeliverySaveRequest();
//
//
//            //when
//            doThrow(DeliveryException.class).when(deliveryRepository)
//                .existsByReservationNumber(any(String.class));
//
//            //then
//            assertThrows(DeliveryException.class,
//                () -> deliveryService.saveDelivery(deliverySaveRequest, invalidMemberId)
//            );
//        }
//
//        @DisplayName("데이터베이스에서 예약 번호 중복 시 DeliveryException을 throw한다.")
//        @Test
//        void test3() {
//            //given
//            Long invalidMemberId = 100L;
//            DeliverySaveRequest deliverySaveRequest = createDeliverySaveRequest();
//
//            //when
//            doThrow(DeliveryException.class).when(deliveryRepository)
//                .save(any(Delivery.class));
//
//            //then
//            assertThrows(DeliveryException.class,
//                () -> deliveryService.saveDelivery(deliverySaveRequest, invalidMemberId)
//            );
//        }
//
//        @DisplayName("배달 저장 성공")
//        @Test
//        void test1000() {
//            //given
//            Long validMemberId = 1L;
//            DeliverySaveRequest deliverySaveRequest = createDeliverySaveRequest();
//
//            //when
//            deliveryService.saveDelivery(deliverySaveRequest, validMemberId);
//
//            //then
//            verify(eventPublisher).publishEvent(any(MemberExistenceCheckEvent.class));
//            verify(deliveryRepository).existsByReservationNumber(any(String.class));
//            verify(deliveryRepository).save(any(Delivery.class));
//            verify(eventPublisher).publishEvent(any(StopOverSavedEvent.class));
//        }
//    }
//
//    @Nested
//    class deleteDelivery {
//
//        @DisplayName("예약 번호로 배달을 찾을 수 없을 때, DeliveryException을 throw한다.")
//        @Test
//        void test1() {
//            //given
//            String invalidReservationNumber = "invalidReservationNumber";
//
//            //when
//            doThrow(DeliveryException.class).when(deliveryRepository)
//                .findByReservationNumber(invalidReservationNumber);
//
//            //then
//            assertThrows(DeliveryException.class, () -> {
//                deliveryService.deleteDelivery(invalidReservationNumber);
//            });
//        }
//
//        @DisplayName("배달 삭제 성공")
//        @Test
//        void test1000() {
//            //given
//            String reservationNumber = "reservationNumber";
//            Delivery delivery = createDummyDelivery();
//
//            when(deliveryRepository.findByReservationNumber(reservationNumber))
//                .thenReturn(Optional.of(delivery));
//
//            //when
//            deliveryService.deleteDelivery(reservationNumber);
//
//            //then
//            verify(deliveryRepository).findByReservationNumber(reservationNumber);
//            verify(eventPublisher).publishEvent(any(StopOverDeletedEvent.class));
//        }
//    }
//
//    @Nested
//    class updateDelivery {
//
//        @DisplayName("예약 번호로 배달을 찾을 수 없을 때, DeliveryException을 throw한다.")
//        @Test
//        void test1() {
//            //given
//            String invalidReservationNumber = "invalidReservationNumber";
//            DeliveryUpdateRequest deliveryUpdateRequest = createDummyDeliveryUpdateRequest();
//
//            //when
//            doThrow(DeliveryException.class).when(deliveryRepository)
//                .findByReservationNumber(invalidReservationNumber);
//
//            //then
//            assertThrows(DeliveryException.class, () -> {
//                deliveryService.updateDelivery(invalidReservationNumber, deliveryUpdateRequest);
//            });
//        }
//
//        @DisplayName("배달 수정 성공, 경유지가 있을 때")
//        @Test
//        void test1000() {
//            //given
//            String reservationNumber = "reservationNumber";
//            DeliveryUpdateRequest deliveryUpdateRequest = createDummyDeliveryUpdateRequest();
//            Delivery delivery = createDummyDelivery();
//
//            when(deliveryRepository.findByReservationNumber(reservationNumber))
//                .thenReturn(Optional.of(delivery));
//
//            //when
//            DeliveryDetailResponse deliveryDetailResponse = deliveryService.updateDelivery(reservationNumber, deliveryUpdateRequest);
//
//            //then
//            verify(deliveryRepository).findByReservationNumber(reservationNumber);
//            verify(eventPublisher).publishEvent(any(StopOverUpdatedEvent.class));
//
//            assertEquals(deliveryDetailResponse.getDeliveryAddress(), deliveryUpdateRequest.getDeliveryAddress());
//            assertEquals(deliveryDetailResponse.getDeliveryDate(), deliveryUpdateRequest.getDeliveryDate());
//        }
//    }
//
//    @Nested
//    class getDeliveryDetail {
//
//        @DisplayName("memberId를 갖는 entity를 찾지 못하면, MemberException을 throw한다.")
//        @Test
//        void test1() {
//            //given
//            String reservationNumber = "reservationNumber";
//            Long invalidMemberId = 100L;
//
//            //when
//            doThrow(MemberException.class).when(eventPublisher)
//                .publishEvent(any(MemberExistenceCheckEvent.class));
//
//            //then
//            assertThrows(MemberException.class,
//                () -> deliveryService.getDeliveryDetail(reservationNumber, invalidMemberId));
//        }
//
//        @DisplayName("예약 번호가 중복되면 DeliveryException을 throw한다.")
//        @Test
//        void test2() {
//            //given
//            String duplicateReservationNumber = "duplicateReservationNumber";
//            Long validMemberId = 1L;
//
//            //when
//            doThrow(DeliveryException.class).when(deliveryRepository)
//                .existsByReservationNumber(any(String.class));
//
//            //then
//            assertThrows(DeliveryException.class,
//                () -> deliveryService.getDeliveryDetail(duplicateReservationNumber, validMemberId));
//        }
//
//        @DisplayName("예약 번호와 회원 ID로 배달을 찾을 수 없을 때, DeliveryException을 throw한다.")
//        @Test
//        void test3() {
//            //given
//            String reservationNumber = "reservationNumber";
//            Long validMemberId = 1L;
//
//            //when
//            //lenient가 없으면 UnnecessaryStubbingException 발생 왜 그런지 모르겠음
//            lenient().doReturn(Optional.empty()).when(deliveryRepository)
//                .findDeliveryByReservationNumberAndMemberId(any(String.class), any(Long.class));
//
//            //then
//            assertThrows(DeliveryException.class,
//                () -> deliveryService.getDeliveryDetail(reservationNumber, validMemberId));
//        }
//
//        @DisplayName("배달 상세 조회 성공")
//        @Test
//        void test1000() {
//            //given
//            String reservationNumber = "reservationNumber";
//            Long validMemberId = 1L;
//            Delivery delivery = createDummyDelivery();
//
//            when(deliveryRepository.existsByReservationNumber(reservationNumber))
//                .thenReturn(true);
//            when(deliveryRepository.findDeliveryByReservationNumberAndMemberId(reservationNumber,
//                validMemberId))
//                .thenReturn(Optional.of(delivery));
//            when(stopOverRepository.findStopOverByDeliveryId(delivery.getId()))
//                .thenReturn(createStopOver());
//
//            //when
//            DeliveryDetailResponse deliveryDetail = deliveryService.getDeliveryDetail(
//                reservationNumber, validMemberId);
//
//            //then
//            verify(eventPublisher).publishEvent(any(MemberExistenceCheckEvent.class));
//            verify(deliveryRepository).existsByReservationNumber(any(String.class));
//            verify(deliveryRepository).findDeliveryByReservationNumberAndMemberId(any(String.class),
//                any(Long.class));
//            verify(stopOverRepository).findStopOverByDeliveryId(any(Long.class));
//
//            assertNotNull(deliveryDetail);
//            assertEquals(reservationNumber, deliveryDetail.getReservationNumber());
//        }
//    }
//
//    @Nested
//    class getDeliveryDetailForDriver {
//
//        @DisplayName("예약 번호로 배달을 찾을 수 없을 때, DeliveryException을 throw한다.")
//        @Test
//        void test1() {
//            //given
//            String invalidReservationNumber = "invalidReservationNumber";
//
//            //when
//            doThrow(DeliveryException.class).when(deliveryRepository)
//                .existsByReservationNumber(invalidReservationNumber);
//
//            //then
//            assertThrows(DeliveryException.class, () -> {
//                deliveryService.getDeliveryDetailForDriver(invalidReservationNumber);
//            });
//        }
//    }
//
//}