package com.example.ssak3.domain.review.model.response;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class ReviewPageResponse<T> {

    private final List<T> content;
    private final Double averageScore;
    private final long totalElements;
    private final int size;
    private final int page;

    private ReviewPageResponse(List<T> content, Double averageScore, long totalElements, int size, int page) {
        this.content = content;
        this.averageScore = averageScore;
        this.totalElements = totalElements;
        this.size = size;
        this.page = page;
    }

    public static <T> ReviewPageResponse<T> from(Page<T> page, Double averageScore) {
        return new ReviewPageResponse<>(
                page.getContent(),
                averageScore,
                page.getTotalElements(),
                page.getSize(),
                page.getNumber());
    }

}
