package com.example.ssak3.domain.s3.model.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ImagePutUrlResponse {

    // presigned url
    private final String uploadUrl;

    // 이미지 url
    private final String imageUrl;

    public static ImagePutUrlResponse from (String uploadUrl, String imageUrl) {
        return new ImagePutUrlResponse(
                uploadUrl,
                imageUrl);
    }

}
