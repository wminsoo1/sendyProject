package com.example.controller.s3;

import com.example.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/s3")
public class S3Controller {

    private final S3Service s3Service;

    @PostMapping("/bucket")
    public ResponseEntity<String> createBucket() {
        s3Service.createBucket();
        return ResponseEntity.ok("Bucket created successfully!");
    }

}