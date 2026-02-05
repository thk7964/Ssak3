package com.example.ssak3.domain.s3.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * 이미지 업로드
     */
    public String uploadImage(MultipartFile multipartFile, String dirName) {
        validateMultipartFile(multipartFile);

        // S3 key 만들기
        String key = buildS3Key(dirName, multipartFile.getOriginalFilename());

        try {
            String uploadImageUrl = putS3(multipartFile, key);

            return uploadImageUrl;

        } catch (IOException e) {
            throw new CustomException(ErrorCode.IMAGE_UPLOAD_ERROR);
        }
    }

    /**
     * 이미지 삭제
     */
    public void deleteImage(String fileUrl) {
        if (fileUrl == null || fileUrl.isBlank()) return;

        int idx = fileUrl.indexOf("amazonaws.com/");
        if (idx < 0) {
            throw new CustomException(ErrorCode.INVALID_URL);
        }

        // "amazonaws.com/" 뒤가 key
        String encodedKey = fileUrl.substring(idx + "amazonaws.com/".length());

        // 한글 파일명 등 퍼센트 인코딩 디코딩
        String key = URLDecoder.decode(encodedKey, StandardCharsets.UTF_8);

        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, key));
    }

    /**
     * S3 업로드
     */
    private String putS3(MultipartFile multipartFile, String key) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        amazonS3Client.putObject(
                bucket,
                key,
                multipartFile.getInputStream(),
                metadata
        );

        return amazonS3Client.getUrl(bucket, key).toString();
    }

    /**
     * S3 key 생성: dirName/uuid_filename 형태
     */
    private String buildS3Key(String dirName, String fileName) {
        return dirName + "/" + UUID.randomUUID() + "_" + fileName;
    }

    private void validateMultipartFile(MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_FILE);
        }
    }
}
