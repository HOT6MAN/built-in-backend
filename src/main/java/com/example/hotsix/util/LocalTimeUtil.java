package com.example.hotsix.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class LocalTimeUtil {
    public static String getDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String date = now.format(format);
        return date;
    }

    public static String getMinusDateByNow(){
        Instant thirtyDaysAgo = Instant.now().minus(0, ChronoUnit.DAYS);
        String date = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.systemDefault())
                .format(thirtyDaysAgo);
        return date;
    }
}
