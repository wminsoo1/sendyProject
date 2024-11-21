package com.example.global.navermap;

import com.example.delivery.Location;
import com.example.delivery.model.DropLocation;
import com.example.delivery.model.LocationFactory;
import com.example.delivery.model.PickUpLocation;
import com.example.global.navermap.dto.LocationResult;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NaverMapGeocode {

    private final Executor naverApiExecutor;
    private final NaverMapClient naverMapClient;

    @Value("${naver.api.client-id}")
    private String clientId;

    @Value("${naver.api.client-secret}")
    private String clientSecret;


    @Autowired
    public NaverMapGeocode(@Qualifier("naverApiExecutor") Executor naverApiExecutor, NaverMapClient naverMapClient) {
        this.naverApiExecutor = naverApiExecutor;
        this.naverMapClient = naverMapClient;
    }

    public PickUpLocation getPickUpLocation(String address) {
        return (PickUpLocation) getLocationFromGecode(address, PickUpLocation::create);
    }

    public DropLocation getDropLocation(String address) {
        return (DropLocation) getLocationFromGecode(address, DropLocation::create);
    }

    @Async("naverApiExecutor")
    public CompletableFuture<JsonNode> getGecodeAsync(String address) {
        return CompletableFuture.supplyAsync(
            () -> naverMapClient.getGecode(address, clientId, clientSecret), naverApiExecutor);
    }

    public Location getLocationFromGecode(String address, LocationFactory locationFactory) {

        try {
            JsonNode addressInfo  = naverMapClient.getGecode(address, clientId, clientSecret);
            JsonNode firstAddress = addressInfo.get("addresses").get(0);

            Double x = firstAddress.get("x").asDouble();
            Double y = firstAddress.get("y").asDouble();

            return locationFactory.create(x, y, address);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse response", e);
        }

    }

    public Location getLocation(JsonNode addressInfo, String address, LocationFactory locationFactory) {
        JsonNode firstAddress = addressInfo.get("addresses").get(0);

        double x = firstAddress.get("x").asDouble();
        double y = firstAddress.get("y").asDouble();

        return locationFactory.create(x, y, address);
    }

    public CompletableFuture<LocationResult> getLocationResultFuture(String pickUpAddress, String dropAddress) {
        CompletableFuture<JsonNode> pickUpFuture = getGecodeAsync(pickUpAddress);
        CompletableFuture<JsonNode> dropFuture = getGecodeAsync(dropAddress);

        return pickUpFuture.thenCombine(dropFuture, LocationResult::new);
    }

}
