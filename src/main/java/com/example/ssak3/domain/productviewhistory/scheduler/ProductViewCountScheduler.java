package com.example.ssak3.domain.productviewhistory.scheduler;

import com.example.ssak3.domain.productviewhistory.service.ProductViewBackUpService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductViewCountScheduler {

    private final ProductViewBackUpService productViewBackUpService;

    // 10분마다 DB에 조회수 백업
    @Scheduled(cron = "* */10 * * * *")
    public void backUpViewCount() {
        productViewBackUpService.backUpViewCount();
    }
}
