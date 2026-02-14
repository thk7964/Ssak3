package com.example.ssak3.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "chatExecutor")
    public Executor chatExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // 기본 스레드 수 (항상 유지)
        executor.setCorePoolSize(5);

        // 최대 스레드 수 (큐가 꽉 찼을 때 증가)
        executor.setMaxPoolSize(10);

        // 대기 큐
        executor.setQueueCapacity(100);

        // 스레드 이름 설정 (로그에서 확인 가능)
        executor.setThreadNamePrefix("chat-async-");

        // 큐가 꽉 찼을 때 호출한 스레드에서 직접 실행 (메시지 손실 방지)
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        executor.initialize();
        return executor;
    }
}