package com.example.driver.model;

import com.example.delivery.exception.DeliveryException;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.example.delivery.exception.DeliveryErrorCode.VEHICLE_NOT_FOUND;


@Getter
@Embeddable
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vehicle {

    @Enumerated(value = EnumType.STRING)
    private VehicleWeight vehicleWeight;

    @Enumerated(value = EnumType.STRING)
    private VehicleType vehicleType;

    public Vehicle(VehicleWeight vehicleWeight, VehicleType vehicleType) {
        this.vehicleWeight = vehicleWeight;
        this.vehicleType = vehicleType;
    }

    public Vehicle update(Vehicle vehicle) {
        validateVehicleExist(vehicle);
        this.vehicleWeight = vehicle.getVehicleWeight();
        this.vehicleType = vehicle.getVehicleType();

        return new Vehicle(vehicle.getVehicleWeight(), vehicle.getVehicleType());
    }

    private void validateVehicleExist(Vehicle vehicle) {
        if (vehicle == null) {
            throw new DeliveryException(VEHICLE_NOT_FOUND);
        }
    }
}
