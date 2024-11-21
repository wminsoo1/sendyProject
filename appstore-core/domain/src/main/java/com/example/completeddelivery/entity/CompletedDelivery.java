package com.example.completeddelivery.entity;

import com.example.delivery.model.DeliveryAddress;
import com.example.delivery.model.DeliveryCategory;
import com.example.delivery.model.DeliveryStatus;
import com.example.driver.model.Vehicle;
import com.example.global.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Builder
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CompletedDelivery extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "driver_id")
    private Long driverId;

    @Column(name = "idempotency_key", unique = true)
    private String idempotencyKey;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "delivery_status", length = 50)
    private DeliveryStatus deliveryStatus; //결제 추가하면 상태도 추가 해줘야 함

    @Column(nullable = false, unique = true)
    @EqualsAndHashCode.Include
    private String reservationNumber;

    @Embedded
    @Column(nullable = false)
    private DeliveryCategory deliveryCategory;

    @Column(nullable = false)
    private LocalDateTime deliveryDate;

    @Embedded
    @Column(nullable = false)
    private Vehicle vehicle;

    @Embedded
    @Column(nullable = false)
    private DeliveryAddress deliveryAddress;

    @Column(nullable = false)
    private BigDecimal deliveryFee;

    private String deliveryOptions;

    @Builder
    private CompletedDelivery(Long id, Long memberId, Long driverId, String idempotencyKey,
        DeliveryStatus deliveryStatus, String reservationNumber, DeliveryCategory deliveryCategory,
        LocalDateTime deliveryDate, Vehicle vehicle, DeliveryAddress deliveryAddress,
        BigDecimal deliveryFee, String deliveryOptions) {
        this.id = id;
        this.memberId = memberId;
        this.driverId = driverId;
        this.idempotencyKey = idempotencyKey;
        this.deliveryStatus = deliveryStatus;
        this.reservationNumber = reservationNumber;
        this.deliveryCategory = deliveryCategory;
        this.deliveryDate = deliveryDate;
        this.vehicle = vehicle;
        this.deliveryAddress = deliveryAddress;
        this.deliveryFee = deliveryFee;
        this.deliveryOptions = deliveryOptions;
    }

}
