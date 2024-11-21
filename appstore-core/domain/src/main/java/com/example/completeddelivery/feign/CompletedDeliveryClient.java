package com.example.completeddelivery.feign;

import com.example.completeddelivery.entity.CompletedDelivery;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "completedDeliveryClient", url = "http://localhost:8081/batch/completed-delivery")
public interface CompletedDeliveryClient {

    @PostMapping
    void saveCompletedDelivery(@RequestBody CompletedDelivery completedDelivery);
}