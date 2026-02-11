package com.example.ssak3.domain.s3.controller;

import com.example.ssak3.common.model.ApiResponse;
import com.example.ssak3.domain.s3.service.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ssak3/admin/images")
@RequiredArgsConstructor
public class S3Controller {

    private final S3Uploader s3Uploader;

    /**
     * 이미지 업로드용 presigned url 발급
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/presigned")
    public ResponseEntity<ApiResponse> createPresignedPutUrl(
            @RequestParam String dirName,
            @RequestParam String fileName
    ) {
        ApiResponse response = ApiResponse.success("presigned url 발급 성공했습니다.", s3Uploader.createPresignedPutUrl(dirName, fileName, 5));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
