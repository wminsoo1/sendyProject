package com.example.delivery.model;

import lombok.Getter;

@Getter
public enum DeliveryType {

    BUSINESS("비즈니스 운송"),
    PERSONAL("개인 물품 운송");

    private final String description;

    DeliveryType(String description) {
        this.description = description;
    }

}