package com.example.delivery.model;

import lombok.Getter;

@Getter
public enum PersonalDeliveryCategory {

    PERSONAL_GENERAL_CARGO("일반 용달"),
    PERSONAL_MOVING_CARGO("용달 이사"),
    PERSONAL_MINI_CARGO("미니 용달");

    private final String description;

    PersonalDeliveryCategory(String description) {
        this.description = description;
    }
}
