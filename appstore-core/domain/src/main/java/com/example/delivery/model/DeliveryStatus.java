package com.example.delivery.model;

import lombok.Getter;

@Getter
public enum DeliveryStatus {
    PAYMENT_PENDING("결제 대기중"),
    ASSIGNMENT_PENDING("배차 대기중"),
    ASSIGNMENT_COMPLETED("배차 완료"),
    DELIVERING("배송 중"),
    DELIVERY_COMPLETED("배송 완료"),
    CANCELED("취소");

    private final String description;

    DeliveryStatus(String description) {
        this.description = description;
    }
}
