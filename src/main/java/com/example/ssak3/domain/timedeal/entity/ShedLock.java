package com.example.ssak3.domain.timedeal.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "shedlock")
public class ShedLock {

    @Id
    @Column(length = 64)
    private String name;

    @Column(nullable = false)
    private LocalDateTime lockUntil;

    @Column(nullable = false)
    private LocalDateTime lockedAt;

    @Column(nullable = false)
    private String lockedBy;
}