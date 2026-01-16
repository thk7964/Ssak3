package com.example.ssak3.domain.category.model.response;

import com.example.ssak3.domain.category.entity.Category;
import com.example.ssak3.domain.product.model.response.ProductListGetResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class CategoryListGetResponse {

    private final Integer counts;
    private final List<CategoryListGetResponse.CategoryDto> productDtoList;

    public static class CategoryDto {
        private Long id;
        private String name;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public CategoryDto(Long id, String name, LocalDateTime createdAt, LocalDateTime updatedAt) {
            this.id = id;
            this.name = name;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }

        public Long getId() {
            return id;
        }
        public String getName() {
            return name;
        }
        public LocalDateTime getCreatedAt() {
            return createdAt;
        }
        public LocalDateTime getUpdatedAt() {
            return updatedAt;
        }
    }
}
