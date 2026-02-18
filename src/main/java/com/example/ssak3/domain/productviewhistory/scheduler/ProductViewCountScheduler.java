package com.example.ssak3.domain.productviewhistory.scheduler;

import com.example.ssak3.domain.productviewhistory.service.ProductViewBackUpService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductViewCountScheduler {

    private final ProductViewBackUpService productViewBackUpService;

    @Scheduled(cron = "* */10 * * * *")
    public void backUpViewCount() {
        productViewBackUpService.backUpViewCount();
    }
}
