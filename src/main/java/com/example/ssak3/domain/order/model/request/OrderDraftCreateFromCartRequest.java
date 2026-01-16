package com.example.ssak3.domain.order.model.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OrderDraftCreateFromCartRequest {

    @NotNull
    private Long cartId;

    @NotNull
    @Size(min = 1)
    private List<Long> productIdList;

}
