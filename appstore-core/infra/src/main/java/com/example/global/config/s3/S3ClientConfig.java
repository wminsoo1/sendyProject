package com.example.global.config.s3;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3ClientConfig {

    @Value("{aws.s3.accessKey}")
    private String accessKey;
    @Value("{aws.s3.secretKey}")
    private String secretKey;

    @Bean
    public AmazonS3 createS3Client() {
        return AmazonS3ClientBuilder.standard()
            .withEndpointConfiguration(
                new AmazonS3ClientBuilder.EndpointConfiguration("http://localhost:4566",
                    "us-east-1"))
            .withCredentials(
                new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
            .withPathStyleAccessEnabled(true)
            .build();
    }

}