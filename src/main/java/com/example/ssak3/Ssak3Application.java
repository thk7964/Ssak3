package com.example.ssak3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@EnableMethodSecurity(prePostEnabled = true)
@EnableCaching
public class Ssak3Application {

    public static void main(String[] args) {
        SpringApplication.run(Ssak3Application.class, args);
    }

}
