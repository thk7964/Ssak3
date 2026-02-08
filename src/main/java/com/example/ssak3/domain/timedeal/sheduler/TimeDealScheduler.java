package com.example.ssak3.domain.timedeal.sheduler;

import com.example.ssak3.common.enums.TimeDealStatus;
import com.example.ssak3.domain.timedeal.entity.TimeDeal;
import com.example.ssak3.domain.timedeal.repository.TimeDealRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class TimeDealScheduler {

    private final TimeDealRepository timeDealRepository;

    @Scheduled(cron = "0 0 * * * *")
    @SchedulerLock(
            name = "timeDealScheduler",
            lockAtMostFor = "PT1M",
            lockAtLeastFor = "PT10S"
    )
    @Transactional
    @CacheEvict(value = "timeDealsOpen", allEntries = true)
    public void updateTimeDealStatus() {
        LocalDateTime now = LocalDateTime.now();

        List<TimeDeal> open = timeDealRepository.findReadyToOpen(now);
        for (TimeDeal timeDeal : open) {
            timeDeal.setStatus(TimeDealStatus.OPEN);
        }

        List<TimeDeal> close = timeDealRepository.findOpenToClose(now);
        for (TimeDeal timeDeal : close) {
            timeDeal.setStatus(TimeDealStatus.CLOSED);
        }
    }
}