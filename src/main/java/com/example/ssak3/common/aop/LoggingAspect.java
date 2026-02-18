package com.example.ssak3.common.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 서비스 메소드에 대한 로그 담당
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspect {

    @Pointcut("execution(* com.example.ssak3.domain..service..*.*(..))")
    private void service() {}

    @Around("service()")
    public Object serviceLogging(ProceedingJoinPoint joinPoint) throws Throwable {

        try {
            return joinPoint.proceed();
        } catch (Throwable e) {
            log.warn("METHOD: {} | MESSAGE: {}", joinPoint.getSignature().toShortString(), e.getMessage());

            throw e;
        }
    }
}
