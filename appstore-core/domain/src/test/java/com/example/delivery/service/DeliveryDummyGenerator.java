package com.example.delivery.service;

import static com.example.delivery.model.DeliveryType.PERSONAL;
import static com.example.delivery.model.PersonalDeliveryCategory.PERSONAL_GENERAL_CARGO;
import static com.example.driver.model.VehicleType.WATERPROOF_COVER;
import static com.example.driver.model.VehicleWeight.RAMBO;

import com.example.delivery.model.DeliveryAddress;
import com.example.delivery.model.DeliveryCategory;
import com.example.delivery.model.DeliveryType;
import com.example.delivery.model.DropLocation;
import com.example.delivery.model.PersonalDeliveryCategory;
import com.example.delivery.model.PickUpLocation;
import com.example.delivery.model.dto.request.DeliverySaveRequest;
import com.example.delivery.model.dto.request.DeliveryUpdateRequest;
import com.example.delivery.model.entity.Delivery;
import com.example.driver.model.Vehicle;
import com.example.driver.model.VehicleType;
import com.example.driver.model.VehicleWeight;
import com.example.stopover.entity.StopOver;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class DeliveryDummyGenerator {

    public static PickUpLocation createPickUpLocation(String address, double lat, double lng) {
        return PickUpLocation.builder()
            .pickUpAddress(address)
            .pickUpLatitude(lat)
            .pickUpLongitude(lng)
            .build();
    }

    public static DropLocation createDropLocation(String address, double lat, double lng) {
        return DropLocation.builder()
            .dropAddress(address)
            .dropLatitude(lat)
            .dropLongitude(lng)
            .build();
    }

    public static Vehicle createVehicle(VehicleWeight weight, VehicleType type) {
        return Vehicle.builder()
            .vehicleWeight(weight)
            .vehicleType(type)
            .build();
    }

    public static DeliveryCategory createDeliveryCategory(DeliveryType deliveryType,
        PersonalDeliveryCategory personalDeliveryCategory) {
        return DeliveryCategory.builder()
            .deliveryType(deliveryType)
            .deliveryPersonalCategory(personalDeliveryCategory)
            .build();
    }

    public static DeliveryAddress createDeliveryAddress(String pickupAddress, String dropAddress) {
        return DeliveryAddress.builder()
            .pickupLocation(createPickUpLocation(pickupAddress, 0.0, 0.0))
            .dropLocation(createDropLocation(dropAddress, 0.0, 0.0))
            .build();
    }

    public static List<DeliveryAddress> createStopOverAddresses() {
        return Arrays.asList(
            createDeliveryAddress("부산시 금정구 부산대학로63번길 2", "부산시 금정구 부산대학로63번길 2"),
            createDeliveryAddress("부산시 금정구 부산대학로63번길 2", "부산시 금정구 부산대학로63번길 2")
        );
    }

    public static DeliverySaveRequest createDeliverySaveRequest() {
        return DeliverySaveRequest.builder()
            .deliveryCategory(createDeliveryCategory(PERSONAL,PERSONAL_GENERAL_CARGO))
            .deliveryDate(LocalDateTime.of(2024, 9, 12, 14, 30))
            .vehicle(createVehicle(VehicleWeight.RAMBO, VehicleType.WATERPROOF_COVER))
            .deliveryAddress(createDeliveryAddress(
                "대구광역시 달서구 송현동2길 31", "부산시 금정구 부산대학로63번길 2"))
            .stopOverAddresses(createStopOverAddresses())
            .deliveryOptions("문 앞에 놓아주세요")
            .build();
    }

    public static DeliveryUpdateRequest createDummyDeliveryUpdateRequest() {
        return new DeliveryUpdateRequest(
            createDeliveryAddress(
                "pickupAddress", "dropAddress"),
            createVehicle(RAMBO, WATERPROOF_COVER),
            LocalDateTime.of(2024, 10, 20, 14, 0),
            "문 앞에 놔두고 사진을 찍어주세요.",
            createStopOverAddresses()
        );
    }

    public static Delivery createDummyDelivery() {
        return Delivery.builder()
            .id(1L)
            .memberId(1L)
            .reservationNumber("reservationNumber")
            .deliveryDate(LocalDateTime.of(2024, 10, 12, 10, 1))
            .deliveryCategory(createDeliveryCategory(PERSONAL, PERSONAL_GENERAL_CARGO))
            .deliveryAddress(createDeliveryAddress(
                "서울특별시 중구 세종대로", "서울특별시 종로구 청와대로 1"))
            .vehicle(createVehicle(VehicleWeight.ONE_TON, VehicleType.WATERPROOF_COVER))
            .deliveryOptions("문 앞에 놓아주세요.")
            .deliveryFee(new BigDecimal("10000"))
            .build();
    }

    public static List<StopOver> createStopOver() {
        StopOver stopOver1 = StopOver.builder()
            .delivery(createDummyDelivery())
            .deliveryAddress(createDeliveryAddress("서울특별시 중구 세종대로", "서울특별시 종로구 청와대로 1"))
            .build();
        StopOver stopOver2 = StopOver.builder()
            .delivery(createDummyDelivery())
            .deliveryAddress(createDeliveryAddress("부산광역시 북구 금곡대로 166", "서울특별시 종로구 청와대로 1"))
            .build();
        return List.of(stopOver1, stopOver2);
    }
}
