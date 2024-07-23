package com.example.hotsix.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class LocalTimeUtil {
    /**
     * @Feature
     * 현재 시간을 String type으로 반환한다.
     * yyyy-MM-dd HH:mm:ss 형태
     * @return String
     */
    public static String getDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String date = now.format(format);
        return date;
    }

    /**
     * @Feature
     * 현재 시간에 -x day를 한 날짜를 반환한다.
     * 반환 타입은 String, 형식은 yyyy-MM-dd HH:mm:ss
     * -x 일을 하는 이유는, 현재 시간 기준 x일 이전 메시지는 전부 삭제하기 위해서
     * @return String
     */
    public static String getMinusDateByNow(){
        Instant thirtyDaysAgo = Instant.now().minus(0, ChronoUnit.DAYS);
        String date = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.systemDefault())
                .format(thirtyDaysAgo);
        return date;
    }

    /**
     * @Feature
     * 현재 시간을 -(음수화)한 String을 반환한다.
     * 형식은 yyyy-MM-dd HH:mm:ss
     * 음수화를 하는 이유는 DynamoDB의 Default가 오름차순 정렬이기 때문에 (내림차순 지원 X)
     * 앞에 음수를 붙여 자연스럽게 내림차순을 만들기 위해서.
     * @return
     */
    public static Long getDescDateTime(){
        LocalDateTime now = LocalDateTime.now();
        return -now.toEpochSecond(ZoneOffset.UTC);
    }
}