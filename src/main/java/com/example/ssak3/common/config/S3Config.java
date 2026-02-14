package com.example.ssak3.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class S3Config {

    @Value("${spring.cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${spring.cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${spring.cloud.aws.region.static}")
    private String region;

    @Bean
    public AwsCredentialsProvider awsCredentialsProvider() {
        return StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey)
        );
    }

    @Bean
    public Region awsRegion() {
        return Region.of(region);
    }

    @Bean
    public S3Client s3Client(AwsCredentialsProvider credentialsProvider, Region awsRegion) {
        return S3Client.builder()
                .credentialsProvider(credentialsProvider)
                .region(awsRegion)
                .build();
    }

    @Bean
    public S3Presigner s3Presigner(AwsCredentialsProvider credentialsProvider, Region awsRegion) {
        return S3Presigner.builder()
                .credentialsProvider(credentialsProvider)
                .region(awsRegion)
                .build();
    }
}
