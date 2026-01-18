package com.example.ssak3.domain.category.model.response;

import com.example.ssak3.domain.category.entity.Category;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class CategoryDeleteResponse {

    private final Long id;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static CategoryDeleteResponse from(Category category) {

        return new CategoryDeleteResponse(
                category.getId(),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }

}
