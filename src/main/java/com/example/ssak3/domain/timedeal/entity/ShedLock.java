package com.example.ssak3.domain.timedeal.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "shedlock")
public class ShedLock {

    @Id
    @Column(length = 64)
    private String name;

    @Column(nullable = false, name = "lock_until")
    private LocalDateTime lockUntil;

    @Column(nullable = false, name = "locked_at")
    private LocalDateTime lockedAt;

    @Column(nullable = false, name = "locked_by")
    private String lockedBy;
}