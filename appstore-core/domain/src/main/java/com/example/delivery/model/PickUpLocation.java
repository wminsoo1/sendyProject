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
public class PickUpLocation implements Location {

    private double pickUpLatitude;
    private double pickUpLongitude;
    private String pickUpAddress;

    private PickUpLocation(double pickUpLatitude, double pickUpLongitude, String pickUpAddress) {
        this.pickUpLatitude = pickUpLatitude;
        this.pickUpLongitude = pickUpLongitude;
        this.pickUpAddress = pickUpAddress;
    }

    public static PickUpLocation create(double latitude, double longitude, String address) {
        return new PickUpLocation(latitude, longitude, address);
    }
}
