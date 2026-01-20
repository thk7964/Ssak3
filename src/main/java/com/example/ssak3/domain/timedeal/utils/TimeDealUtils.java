package com.example.ssak3.domain.timedeal.utils;

import java.time.Duration;
import java.time.LocalDateTime;

public class TimeDealUtils {
    private TimeDealUtils() {
    }

    public static String formatRemainingTime(LocalDateTime now, LocalDateTime startAt, LocalDateTime endAt) {
        LocalDateTime tagetTime;

        if (now.isBefore(startAt)) {
            tagetTime = startAt;
        } else if (now.isBefore(endAt)) {
            tagetTime = endAt;
        } else {
            return "00:00:00:00";
        }

        long totalSeconds = Duration.between(now, tagetTime).getSeconds();

        long dd = totalSeconds / (24 * 3600);
        totalSeconds %= 24 * 3600;

        long hh = totalSeconds / 3600;
        totalSeconds %= 3600;

        long mm = totalSeconds / 60;
        long ss = totalSeconds % 60;

        return  String.format("%02d:%02d:%02d:%02d",dd,hh,mm,ss);
    }
}
