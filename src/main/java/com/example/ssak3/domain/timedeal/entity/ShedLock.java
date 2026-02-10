package com.example.ssak3.domain.timedeal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "shedlock")
public class ShedLock {

    @Id
    @Column(length = 64)
    private String name;

    @Column(nullable = false)
    private LocalDateTime lockUntil;        // 락 만료시간

    @Column(nullable = false)
    private LocalDateTime lockedAt;         // 락 획득 시간

    @Column(nullable = false)
    private String lockedBy;                // 락 식별자
}