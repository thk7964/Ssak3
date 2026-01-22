package com.example.ssak3.domain.timedeal.sheduler;

import com.example.ssak3.common.enums.TimeDealStatus;
import com.example.ssak3.domain.product.repository.ProductRepository;
import com.example.ssak3.domain.timedeal.repository.TimeDealRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class TimeDealScheduler {

    private final TimeDealRepository timeDealRepository;
    private final ProductRepository productRepository;

    @Scheduled(fixedRate = 60_000) //1분마다 실행
    @Transactional
    public void updateTimeDealStatus() {
        LocalDateTime now = LocalDateTime.now();

        timeDealRepository.findAllByStatus(TimeDealStatus.READY).forEach(timeDeal -> {

            if (!timeDeal.isDeleted() && now.isAfter(timeDeal.getStartAt())) {
                timeDeal.setStatus(TimeDealStatus.OPEN);
                log.info("타임딜 id : "+timeDeal.getId()+" 타임딜 상태 : "+timeDeal.getStatus() +" 상품 상태 :" + timeDeal.getProduct().getStatus());
                productRepository.save(timeDeal.getProduct());
                timeDealRepository.save(timeDeal);
            }
        });

        timeDealRepository.findAllByStatus(TimeDealStatus.OPEN).forEach(timeDeal -> {

            if (!timeDeal.isDeleted() && now.isAfter(timeDeal.getEndAt())) {

                timeDeal.setStatus(TimeDealStatus.CLOSED);
                log.info("타임딜 id : "+timeDeal.getId()+" 타임딜 상태 : "+timeDeal.getStatus() +" 상품 상태 :" + timeDeal.getProduct().getStatus());

                productRepository.save(timeDeal.getProduct());
                timeDealRepository.save(timeDeal);
            }
        });
    }
}