package com.example.driver.service;

import com.example.driver.model.Vehicle;
import com.example.driver.model.VehicleType;
import com.example.driver.model.VehicleWeight;
import com.example.driver.model.dto.request.CreateDriverRequest;
import com.example.driver.model.dto.request.SignInDriverRequest;
import com.example.driver.model.entity.Driver;

public class DriverDummyGenerator {

    public static CreateDriverRequest createDriverRequest(String driverName, String password, Vehicle vehicle) {
        return CreateDriverRequest.builder()
            .driverName(driverName)
            .password(password)
            .vehicle(vehicle)
            .build();
    }

    public static Vehicle createDummyVehicle() {
        return Vehicle.builder()
            .vehicleWeight(VehicleWeight.ONE_TON)
            .vehicleType(VehicleType.CARGO)
            .build();
    }

    public static SignInDriverRequest creatDummySignInDriverRequest() {
        return SignInDriverRequest
            .builder()
            .driverName("사용자")
            .password("1234567a")
            .build();
    }

    public static Driver createDummyDriver() {
        return Driver.builder()
            .id(1L)
            .driverName("운전자")
            .password("1234567a")
            .build();
    }

}
