package com.example.delivery.model;

import com.example.delivery.Location;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DropLocation implements Location {

    private double dropLatitude;
    private double dropLongitude;
    private String dropAddress;

    private DropLocation(double dropLatitude, double dropLongitude, String dropAddress) {
        this.dropLatitude = dropLatitude;
        this.dropLongitude = dropLongitude;
        this.dropAddress = dropAddress;
    }

    public static DropLocation create(double latitude, double longitude, String address) {
        return new DropLocation(latitude, longitude, address);
    }
}
