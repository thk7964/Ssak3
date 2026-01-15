package com.example.ssak3.domain.product.model.response;

import com.example.ssak3.domain.product.entity.Product;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class ProductGetListResponse {

    private final Integer counts;
    private final List<ProductDto> productDtoList;

    public static class ProductDto {
        private Long id;
        private String name;
        private Integer price;
        private String status;
        private String information;
        private Integer quantity;
        private final LocalDateTime createdAt;
        private final LocalDateTime updatedAt;

        public ProductDto(Long id, String name, Integer price, String status, String information, Integer quantity, LocalDateTime createdAt, LocalDateTime updatedAt) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.status = status;
            this.information = information;
            this.quantity = quantity;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }

        public Long getId() {
            return id;
        }
        public String getName() {
            return name;
        }
        public Integer getPrice() {
            return price;
        }
        public String getStatus() {
            return status;
        }
        public String getInformation() {
            return information;
        }
        public Integer getQuantity() {
            return quantity;
        }
        public LocalDateTime getCreatedAt() {
            return createdAt;
        }
        public LocalDateTime getUpdatedAt() {
            return updatedAt;
        }
    }
}
