package com.team11.backend.timeConversion;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.Locale;

public class MessageTimeConversion {
    public static String timeConversion(LocalDateTime modifiedAt) {
        LocalDateTime currentTime = LocalDateTime.now();
        Long timeDiff = Duration.between(modifiedAt, currentTime).getSeconds();
        String resultConversion = "";

        if((timeDiff / 60 / 60 / 24 / 7) > 0){//주
            resultConversion = modifiedAt.getMonth()+"월 "+modifiedAt.getDayOfMonth()+"일";
        }
        else if ((timeDiff / 60 / 60 / 24) > 0) { // 일
            resultConversion = timeDiff / 60 / 60 / 24 + "일 전";
        } else{
            if(modifiedAt.get(ChronoField.AMPM_OF_DAY) == 0) {
                resultConversion = "오전 "+modifiedAt.getHour()+":"+modifiedAt.getMinute();
            } else {
                resultConversion = "오후 "+modifiedAt.getHour()+":"+modifiedAt.getMinute();
            }
        }
        return resultConversion;
    }
}
