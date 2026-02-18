package com.example.ssak3.domain.product.service;

import com.example.ssak3.common.enums.TimeDealStatus;
import com.example.ssak3.domain.product.entity.Product;
import com.example.ssak3.domain.timedeal.entity.TimeDeal;
import com.example.ssak3.domain.timedeal.repository.TimeDealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final TimeDealRepository timeDealRepository;
    private final RedisCacheManager redisCacheManager;

    @Transactional
    public void decreaseProductStock(Product product, int quantity) {
        product.decreaseQuantity(quantity);

        if (product.getQuantity() == 0) {
            List<TimeDeal> openDeals = timeDealRepository.findOpenByProductId(product.getId());
            openDeals.forEach(timeDeal -> timeDeal.setStatus(TimeDealStatus.CLOSED));

            timeDealRepository.saveAll(openDeals);

            redisCacheManager.getCache("timeDealsOpen").clear();
        }
    }
}
