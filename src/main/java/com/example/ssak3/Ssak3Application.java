package com.example.ssak3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Ssak3Application {

    public static void main(String[] args) {
        SpringApplication.run(Ssak3Application.class, args);
    }

}
