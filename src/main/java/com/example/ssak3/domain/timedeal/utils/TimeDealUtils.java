package com.example.ssak3.domain.timedeal.utils;

import java.time.Duration;
import java.time.LocalDateTime;

public class TimeDealUtils {
    private TimeDealUtils() {
    }

    public static String formatRemainingTime(LocalDateTime now, LocalDateTime startAt, LocalDateTime endAt) {
        LocalDateTime targetTime;

        if (now.isBefore(startAt)) {        //open전까지 남은시간
            targetTime = startAt;
        } else if (now.isBefore(endAt)) {   //closed전까지 남음시간
            targetTime = endAt;
        } else {
            return "00:00:00:00";
        }

        long totalSeconds = Duration.between(now, targetTime).getSeconds();

        long dd = totalSeconds / (24 * 3600);   // 날짜
        totalSeconds %= 24 * 3600;

        long hh = totalSeconds / 3600;          // 시간
        totalSeconds %= 3600;

        long mm = totalSeconds / 60;            // 분
        long ss = totalSeconds % 60;            // 초

        return  String.format("%02d:%02d:%02d:%02d",dd,hh,mm,ss);
    }
}
