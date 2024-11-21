package com.example.driver.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class DriverAddress {
    private String street;
}
