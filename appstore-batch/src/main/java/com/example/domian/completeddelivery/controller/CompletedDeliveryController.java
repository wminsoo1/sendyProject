package com.example.domian.completeddelivery.controller;

import com.example.completeddelivery.entity.CompletedDelivery;
import com.example.completeddelivery.service.CompletedDeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/batch/completed-delivery")
@RestController
@RequiredArgsConstructor
public class CompletedDeliveryController {

    private final CompletedDeliveryService completedDeliveryService;

    @PostMapping
    public ResponseEntity<CompletedDelivery> complete(@RequestBody CompletedDelivery completedDelivery) {
        CompletedDelivery saveCompletedDelivery = completedDeliveryService.saveCompletedDelivery(completedDelivery);

        return ResponseEntity.status(HttpStatus.CREATED).body(saveCompletedDelivery);
    }

}

