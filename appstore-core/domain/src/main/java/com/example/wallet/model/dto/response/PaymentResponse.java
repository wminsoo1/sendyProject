package com.example.wallet.model.dto.response;

import com.example.delivery.model.DeliveryAddress;
import com.example.delivery.model.PersonalDeliveryCategory;
import com.example.delivery.model.entity.Delivery;
import com.example.driver.model.Vehicle;
import com.example.wallet.model.entity.Wallet;
import jakarta.persistence.Embedded;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentResponse {

    BigDecimal paymentBalance;

    BigDecimal balance;

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

    private PaymentResponse(BigDecimal paymentBalance, BigDecimal balance, String reservationNumber, PersonalDeliveryCategory deliveryPersonalCategory, LocalDateTime deliveryDate, DeliveryAddress deliveryAddress, List<DeliveryAddress> stopOverAddresses, Vehicle vehicle, String deliveryOptions) {
        this.paymentBalance = paymentBalance;
        this.balance = balance;
        this.reservationNumber = reservationNumber;
        this.deliveryPersonalCategory = deliveryPersonalCategory;
        this.deliveryDate = deliveryDate;
        this.deliveryAddress = deliveryAddress;
        this.stopOverAddresses = stopOverAddresses;
        this.vehicle = vehicle;
        this.deliveryOptions = deliveryOptions;
    }

    public static PaymentResponse from(BigDecimal paymentBalance, Wallet wallet, Delivery delivery, List<DeliveryAddress> stopOverAddresses) {
        return new PaymentResponse(
                paymentBalance,
                wallet.getBalance(),
                delivery.getReservationNumber(),
                delivery.getDeliveryCategory().getDeliveryPersonalCategory(),
                delivery.getDeliveryDate(),
                delivery.getDeliveryAddress(),
                stopOverAddresses,
                delivery.getVehicle(),
                delivery.getDeliveryOptions()
        );
    }
}
