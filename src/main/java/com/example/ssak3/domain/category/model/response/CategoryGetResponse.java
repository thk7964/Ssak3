package com.example.ssak3.domain.category.model.response;

import com.example.ssak3.domain.category.entity.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CategoryGetResponse {

    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CategoryGetResponse(Long id, String name, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static CategoryGetResponse from(Category category) {
       return new CategoryGetResponse(
               category.getId(),
               category.getName(),
               category.getCreatedAt(),
               category.getUpdatedAt()
       );
    }
}
