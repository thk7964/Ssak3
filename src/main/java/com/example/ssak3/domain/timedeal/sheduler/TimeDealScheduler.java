package com.example.ssak3.domain.timedeal.sheduler;

import com.example.ssak3.common.enums.TimeDealStatus;
import com.example.ssak3.domain.timedeal.entity.TimeDeal;
import com.example.ssak3.domain.timedeal.repository.TimeDealRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class TimeDealScheduler {

    private final TimeDealRepository timeDealRepository;

    /** 로그용 인스턴스 식별자 */
    private String instanceId;

    @PostConstruct
    void init() {
        try {
            this.instanceId = InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            this.instanceId = "unknown-host";
        }
    }

    @Scheduled(cron = "0 * * * * *")
    @SchedulerLock(
            name = "timeDealScheduler",
            lockAtMostFor = "PT1M",
            lockAtLeastFor = "PT10S"
    )
    @Transactional
    public void updateTimeDealStatus() {
        LocalDateTime now = LocalDateTime.now();
        log.info("[{}] TimeDeal Scheduler START at {}", instanceId, now);

        List<TimeDeal> open = timeDealRepository.findReadyToOpen(now);
        for (TimeDeal timeDeal : open) {
            timeDeal.setStatus(TimeDealStatus.OPEN);
            log.info("[{}] OPEN dealId={}", instanceId, timeDeal.getId());
        }

        List<TimeDeal> close = timeDealRepository.findOpenToClose(now);
        for (TimeDeal timeDeal : close) {
            timeDeal.setStatus(TimeDealStatus.CLOSED);
            log.info("[{}] CLOSE dealId={}", instanceId, timeDeal.getId());
        }

    }
}