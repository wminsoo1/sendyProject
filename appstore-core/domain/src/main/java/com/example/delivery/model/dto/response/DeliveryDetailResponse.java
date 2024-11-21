package com.example.delivery.model.dto.response;

import com.example.delivery.model.DeliveryAddress;
import com.example.delivery.model.PersonalDeliveryCategory;
import com.example.delivery.model.entity.Delivery;
import com.example.driver.model.Vehicle;
import jakarta.persistence.Embedded;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.ToString;


@Getter
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DeliveryDetailResponse {

    private String reservationNumber;

    @Enumerated(EnumType.STRING)
    private PersonalDeliveryCategory deliveryPersonalCategory;

    private LocalDateTime deliveryDate;

    @Embedded
    private DeliveryAddress deliveryAddress;

    @Embedded
    private List<DeliveryAddress> stopOverAddresses;

    @Embedded
    private Vehicle vehicle;

    private String deliveryOptions;

    private BigDecimal deliveryFee; //BigDemical

    private DeliveryDetailResponse(String reservationNumber, PersonalDeliveryCategory deliveryPersonalCategory, LocalDateTime deliveryDate, DeliveryAddress deliveryAddress, List<DeliveryAddress> stopOverAddresses, Vehicle vehicle, String deliveryOptions, BigDecimal deliveryFee) {
        this.reservationNumber = reservationNumber;
        this.deliveryPersonalCategory = deliveryPersonalCategory;
        this.deliveryDate = deliveryDate;
        this.deliveryAddress = deliveryAddress;
        this.stopOverAddresses = stopOverAddresses;
        this.vehicle = vehicle;
        this.deliveryOptions = deliveryOptions;
        this.deliveryFee = deliveryFee;
    }

    public static DeliveryDetailResponse from(Delivery delivery, List<DeliveryAddress> stopOverAddresses) {
        return new DeliveryDetailResponse(
                delivery.getReservationNumber(),
                delivery.getDeliveryCategory().getDeliveryPersonalCategory(),
                delivery.getDeliveryDate(),
                delivery.getDeliveryAddress(),
                stopOverAddresses,
                delivery.getVehicle(),
                delivery.getDeliveryOptions(),
                delivery.getDeliveryFee());
    }

}
