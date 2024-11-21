package com.example.global.navermap.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

@Getter
public class LocationResult {

    private final JsonNode pickUpResult;
    private final JsonNode dropResult;

    public LocationResult(JsonNode pickUpResult, JsonNode dropResult) {
        this.pickUpResult = pickUpResult;
        this.dropResult = dropResult;
    }
}
