package com.example.ssak3.domain.product.scheduler;

import com.example.ssak3.domain.product.service.ProductRankingService;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductWeeklyRankingScheduler {

    private final ProductRankingService productRankingService;

    /**
     * 1분마다 주간 인기 집계
     */
    @Scheduled(fixedDelay = 60000, initialDelay = 0)
    @SchedulerLock(name = "updateWeeklyRankingLock", lockAtMostFor = "50s", lockAtLeastFor = "10s")
    public void updateWeeklyRanking() {
        productRankingService.updateWeeklyRanking();
    }

}
