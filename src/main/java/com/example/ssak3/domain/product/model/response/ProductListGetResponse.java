package com.example.ssak3.domain.product.model.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class ProductListGetResponse {

    private final Integer counts;
    private final List<ProductListGetResponse.ProductDto> productDtoList;

    public static class ProductDto {
        private Long id;
        private Long categoryId;
        private String name;
        private Integer price;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public ProductDto(Long id, Long categoryId, String name, Integer price, LocalDateTime createdAt, LocalDateTime updatedAt) {
            this.id = id;
            this.categoryId = categoryId;
            this.name = name;
            this.price = price;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }

        public Long getId() {
            return id;
        }
        public Long getCategoryId() {return categoryId;}
        public String getName() {
            return name;
        }
        public Integer getPrice() {
            return price;
        }
        public LocalDateTime getCreatedAt() {
            return createdAt;
        }
        public LocalDateTime getUpdatedAt() {
            return updatedAt;
        }
    }
}
