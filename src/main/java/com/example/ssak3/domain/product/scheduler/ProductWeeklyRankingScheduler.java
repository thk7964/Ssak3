package com.example.ssak3.domain.product.scheduler;

import com.example.ssak3.domain.product.service.ProductRankingService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductWeeklyRankingScheduler {

    private final ProductRankingService productRankingService;

    /**
     * 프로그램 처음 실행 시 한 번 집계
     */
    @PostConstruct
    public void init() {
        productRankingService.updateWeeklyRanking();
    }

    /**
     * 1분마다 주간 인기 집계
     */
    @Scheduled(cron = "0 */1 * * * *")
    public void updateWeeklyRanking() {
        productRankingService.updateWeeklyRanking();
    }

}
