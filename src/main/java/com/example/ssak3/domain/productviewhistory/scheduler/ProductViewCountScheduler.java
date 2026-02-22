package com.example.ssak3.domain.productviewhistory.scheduler;

import com.example.ssak3.domain.productviewhistory.service.ProductViewBackUpService;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductViewCountScheduler {

    private final ProductViewBackUpService productViewBackUpService;

    @Scheduled(cron = "0 */10 * * * *")
    @SchedulerLock(name = "backupViewCountLock", lockAtMostFor = "9m", lockAtLeastFor = "1m")
    public void backUpViewCount() {
        productViewBackUpService.backUpViewCount();
    }
}
