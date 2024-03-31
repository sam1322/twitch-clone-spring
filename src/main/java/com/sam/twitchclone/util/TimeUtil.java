package com.sam.twitchclone.util;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class TimeUtil {
    public static ZonedDateTime toInstantWithTimeZone(Instant instant, String zoneId) {
        ZoneId zone = ZoneId.of(zoneId);
        ZonedDateTime zonedDateTime = instant.atZone(zone);
        return zonedDateTime;
    }
//    public ZonedDateTime getCurrentTime(){
//        Instant currentTime = Instant.now();
//        return toInstantWithTimeZone(currentTime, "Asia/Kolkata");
//    }

    public Instant getCurrentTime(){
        Instant currentTime = Instant.now();
        ZonedDateTime zonedDateTime = toInstantWithTimeZone(currentTime, "Asia/Kolkata");
        return zonedDateTime.toInstant();
    }
}
