package com.example.synchronous.geotracking.common.util;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    public static OffsetDateTime parseToOffsetDateTime(String date){
        return  date == null ? null : OffsetDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME);
    }

    public static Instant parseToInstant(OffsetDateTime date){
        return date == null ? null : date.toInstant();
    }

}
