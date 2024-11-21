package com.example.driver.model;

import lombok.Getter;

@Getter
public enum VehicleType {

    CARGO("카고"),
    WATERPROOF_COVER("방수 덮개"),
    TOP_CAR("탑차"),
    WING_BODY("윙바디"),
    LIFT("리프트"),
    REFRIGERATED("냉장"),
    FREEZER("냉동");

    private final String description;

    VehicleType(String description) {
        this.description = description;
    }

}
