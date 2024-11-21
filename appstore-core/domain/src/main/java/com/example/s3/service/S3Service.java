package com.example.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class S3Service {

    private static final String BUCKET_NAME = "uploads-bucket";
    private static final String S3_URL = "http://localhost:4566";

    private final AmazonS3 amazonS3;


    public void createBucket() {
        if (!amazonS3.doesBucketExistV2(BUCKET_NAME)) {
            amazonS3.createBucket(BUCKET_NAME);
        }
    }

    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        amazonS3.putObject(new PutObjectRequest(BUCKET_NAME, fileName, file.getInputStream(), metadata));

        return S3_URL + BUCKET_NAME + "/" + fileName;
    }

}
