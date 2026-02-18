package com.example.ssak3.domain.s3.service;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.domain.s3.model.response.ImagePutUrlResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class S3Uploader {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * put presigned url 생성
     */
    public ImagePutUrlResponse createPresignedPutUrl(String dirName, String originalFilename, int expireMinutes) {

        if (originalFilename == null || originalFilename.isBlank()) {
            throw new CustomException(ErrorCode.INVALID_FILE);
        }

        String key = buildS3Key(dirName, originalFilename);

        PutObjectRequest putReq = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        PutObjectPresignRequest presignReq = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(expireMinutes))
                .putObjectRequest(putReq)
                .build();

        PresignedPutObjectRequest presigned = s3Presigner.presignPutObject(presignReq);

        String uploadUrl = presigned.url().toString();
        String imageUrl = getPublicUrl(bucket, key).toString();

        return ImagePutUrlResponse.from(uploadUrl, imageUrl);
    }

    /**
     * get presigned url 생성
     */
    public String createPresignedGetUrl(String fileUrl, int expireMinutes) {

        if (fileUrl == null) {
            return null;
        }

        String key = extractKey(fileUrl);

        GetObjectPresignRequest presignReq = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(expireMinutes))
                .getObjectRequest(r -> r.bucket(bucket).key(key))
                .build();

        PresignedGetObjectRequest presigned = s3Presigner.presignGetObject(presignReq);

        return presigned.url().toString();
    }

    /**
     * url에서 key 추출
     */
    public String extractKey(String fileUrl) {

        if (fileUrl == null || fileUrl.isBlank()) {
            throw new CustomException(ErrorCode.INVALID_URL);
        }

        int idx = fileUrl.indexOf("amazonaws.com/");
        if (idx < 0) {
            throw new CustomException(ErrorCode.INVALID_URL);
        }

        String encodedKey = fileUrl.substring(idx + "amazonaws.com/".length());
        return URLDecoder.decode(encodedKey, StandardCharsets.UTF_8);
    }

    /**
     * 이미지 삭제
     */
    public void deleteImage(String fileUrl) {

        if (fileUrl == null || fileUrl.isBlank()) return;

        String key = extractKey(fileUrl);

        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build());
    }

    /**
     * S3 key 생성
     */
    private String buildS3Key(String dirName, String fileName) {
        return dirName + "/" + UUID.randomUUID() + "_" + fileName;
    }

    private URL getPublicUrl(String bucket, String key) {
        return s3Client.utilities().getUrl(GetUrlRequest.builder()
                .bucket(bucket)
                .key(key)
                .build());
    }
}
