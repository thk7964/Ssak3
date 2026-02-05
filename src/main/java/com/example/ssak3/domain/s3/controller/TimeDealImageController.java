package com.example.ssak3.domain.s3.controller;

import com.example.ssak3.common.model.ApiResponse;
import com.example.ssak3.domain.s3.service.TimeDealImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ssak3/time-deals/{timeDealId}/images")
public class TimeDealImageController {

    private final TimeDealImageService timeDealImageService;

    /**
     * 타임딜 이미지 업로드
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse> createTimeDealImageApi(@PathVariable Long timeDealId, @RequestPart(value = "image") MultipartFile file) throws IOException {

        ApiResponse response = ApiResponse.success("타임딜 이미지 저장에 성공했습니다.", timeDealImageService.uploadTimeDealImage(timeDealId, file));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 타임딜 이미지 가져오기
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse> getTimeDealImageApi(@PathVariable Long timeDealId){
        ApiResponse response = ApiResponse.success("타임딜 이미지 조회에 성공했습니다.", timeDealImageService.getTimeDealImage(timeDealId));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 타임딜 이미지 수정
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping
    public ResponseEntity<ApiResponse> UpdateTimeDealImageApi(@PathVariable Long timeDealId, @RequestPart(value = "image")  MultipartFile file) throws IOException {

        ApiResponse response = ApiResponse.success("타임딜 이미지 변경에 성공했습니다.", timeDealImageService.updateTimeDealImage(timeDealId, file));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 타임딜 이미지 삭제
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping
    public ResponseEntity<ApiResponse> deleteTimeDealImageApi(@PathVariable Long timeDealId){

        ApiResponse response = ApiResponse.success("타임딜 이미지 삭제에 성공했습니다.", timeDealImageService.deleteTimeDealImage(timeDealId));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
