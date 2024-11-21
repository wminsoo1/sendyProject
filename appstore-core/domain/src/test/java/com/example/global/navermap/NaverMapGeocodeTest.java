package com.example.global.navermap;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.delivery.model.DropLocation;
import com.example.delivery.model.PickUpLocation;
import com.example.TestConfiguration;
import com.example.global.config.AsyncConfig;
import com.example.global.config.RestTemplateConfig;
import com.example.global.navermap.dto.LocationResult;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {TestConfiguration.class, AsyncConfig.class})
@Import(RestTemplateConfig.class)
@SpringBootTest
@EnableFeignClients(basePackages = "com.example.global.navermap") // 추가
class NaverMapGeocodeTest {

    @Autowired
    NaverMapGeocode naverMapGeocode;

    @Autowired
    @Qualifier("naverApiExecutor")
    private Executor naverApiExecutor;

    @DisplayName("비동기 테스트")
    @Test
    void test1() throws ExecutionException, InterruptedException {
        CompletableFuture<JsonNode> gecodeAsync = naverMapGeocode.getGecodeAsync(
            "대구광역시 달서구 송현동2길 31");

        JsonNode jsonNode = gecodeAsync.get();
        System.out.println("jsonNode = " + jsonNode);
    }

    @DisplayName("비동기 테스트 로그 확인")
    @Test
    void test2() throws ExecutionException, InterruptedException {
        CompletableFuture<LocationResult> locationResultFuture = naverMapGeocode.getLocationResultFuture(
            "대구광역시 달서구 송현동2길 31", "부산시 금정구 부산대학로63번길 2");

        LocationResult locationResult = locationResultFuture.get();
        System.out.println("locationResult = " + locationResult);
    }

    @DisplayName("비동기 테스트 로그 확인")
    @Test
    void test3() throws ExecutionException, InterruptedException {
        int iterations = 5; // 반복 횟수
        long syncTotalTime = 0;
        long asyncTotalTime = 0;

        // 동기 작업 시간 측정
        for (int i = 0; i < iterations; i++) {
            long startTime = System.currentTimeMillis();
            PickUpLocation pickUpLocation = (PickUpLocation) naverMapGeocode.getLocationFromGecode(
                "대구광역시 달서구 송현동2길 31",
                PickUpLocation::create);
            System.out.println("동기 작업 결과: " + pickUpLocation);
            DropLocation dropLocation = (DropLocation) naverMapGeocode.getLocationFromGecode(
                "대구광역시 달서구 송현동2길 31",
                DropLocation::create);
            System.out.println("동기 작업 결과: " + dropLocation);
            long endTime = System.currentTimeMillis();
            syncTotalTime += (endTime - startTime);
        }

        // 비동기 작업 시간 측정
        for (int i = 0; i < iterations; i++) {
            long startTime = System.currentTimeMillis();
            CompletableFuture<LocationResult> locationResultFuture = naverMapGeocode.getLocationResultFuture(
                "대구광역시 달서구 송현동2길 31", "부산시 금정구 부산대학로63번길 2");
            LocationResult locationResult = locationResultFuture.get(); // 결과를 기다림
            long endTime = System.currentTimeMillis();
            asyncTotalTime += (endTime - startTime);
            System.out.println("비동기 작업 결과: " + locationResult);
        }

        System.out.println("총 동기 작업 소요 시간: " + syncTotalTime + "ms");
        System.out.println("총 비동기 작업 소요 시간: " + asyncTotalTime + "ms");
    }

    @DisplayName("비동기 작업 거부 테스트")
    @Test
    void testRejectedExecution() throws InterruptedException {
        // ExecutorService를 사용하여 고정된 스레드 풀 생성
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        // 100개 이상의 비동기 작업 요청
        for (int i = 0; i < 150; i++) {
            executorService.submit(() -> {
                try {
                    // 비동기 작업 시뮬레이션
                    naverApiExecutor.execute(() -> {
                        try {
                            // 비동기 작업을 수행하는 것처럼 보이게 하기 위해 sleep
                            Thread.sleep(1000);
                            System.out.println(Thread.currentThread().getName() + " - 작업 완료");
                        } catch (InterruptedException e) {
                            // 스레드가 인터럽트될 경우
                            Thread.currentThread().interrupt();
                        }
                    });
                } catch (Exception e) {
                    // 비동기 작업이 거부되면 예외 처리
                    System.err.println("작업 거부: " + e.getMessage());
                }
            });
        }

        // 모든 작업이 완료될 때까지 대기
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);
    }
}