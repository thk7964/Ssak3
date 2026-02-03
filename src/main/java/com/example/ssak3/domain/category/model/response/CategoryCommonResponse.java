package com.example.ssak3.domain.category.model.response;

import com.example.ssak3.domain.category.entity.Category;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;


@Getter
@RequiredArgsConstructor
public class CategoryCommonResponse {

    private final Long id;
    private final String name;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static CategoryCommonResponse from(Category category) {
       return new CategoryCommonResponse(
               category.getId(),
               category.getName(),
               category.getCreatedAt(),
               category.getUpdatedAt()
       );
    }
}
