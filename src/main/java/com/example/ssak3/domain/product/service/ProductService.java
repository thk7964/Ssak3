package com.example.ssak3.domain.product.service;

import com.example.ssak3.domain.product.entity.Product;
import com.example.ssak3.domain.product.model.request.ProductCreateRequest;
import com.example.ssak3.domain.product.model.response.ProductCreateResponse;
import com.example.ssak3.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * 상품생성 비즈니스 로직
     */
    @Transactional
    public ProductCreateResponse createProduct(ProductCreateRequest request) {

        Product product = new Product(
                request.getName(),
                request.getPrice(),
                request.getStatus(),
                request.getInformation(),
                request.getQuantity()
        );
        Product createdProduct = productRepository.save(product);
        return ProductCreateResponse.from(createdProduct);
    }
}
