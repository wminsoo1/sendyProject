package com.example.delivery.model;

import com.example.delivery.Location;

@FunctionalInterface
public interface LocationFactory {
    Location create(double latitude, double longitude, String address);
}
