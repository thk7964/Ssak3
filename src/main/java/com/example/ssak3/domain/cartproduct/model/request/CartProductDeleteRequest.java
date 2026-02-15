package com.example.ssak3.domain.cartproduct.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CartProductDeleteRequest {

    @NotNull
    private Long cartProductId;

}
