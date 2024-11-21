package com.example.global.navermap;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "naverMapClient", url = "https://naveropenapi.apigw.ntruss.com")
public interface NaverMapClient {

    @GetMapping("/map-geocode/v2/geocode")
    JsonNode getGecode(
        @RequestParam("query") String query,
        @RequestHeader("X-NCP-APIGW-API-KEY-ID") String clientId,
        @RequestHeader("X-NCP-APIGW-API-KEY") String clientSecret
    );
}
