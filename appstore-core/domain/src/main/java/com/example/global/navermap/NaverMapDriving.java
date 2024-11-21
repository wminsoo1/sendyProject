package com.example.global.navermap;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class NaverMapDriving {

    @Value("${naver.api.client-id}")
    private String clientId;

    @Value("${naver.api.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate;


    public String getDistance(String start, String goal) {
        String url = String.format("https://naveropenapi.apigw.ntruss.com/map-direction/v1/driving?start=%s&goal=%s&option=traoptimal", start, goal);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-NCP-APIGW-API-KEY-ID", clientId);
        headers.set("X-NCP-APIGW-API-KEY", clientSecret);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

        // 응답에서 거리 정보 추출 (JSON 파싱)
        // JSON 파싱을 위해 추가적인 라이브러리 (예: Jackson, Gson) 사용 필요

        return response.getBody();
    }
}