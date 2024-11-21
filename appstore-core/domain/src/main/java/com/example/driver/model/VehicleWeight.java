package com.example.driver.model;

import lombok.Getter;

@Getter
public enum VehicleWeight {

    DAMAS("다마스"),
    RAMBO("람보"),
    ONE_TON("1톤"),
    THREE_TON("3톤"),
    FIVE_TON("5톤");

    private final String description;

    VehicleWeight(String description) {
        this.description = description;
    }

}
