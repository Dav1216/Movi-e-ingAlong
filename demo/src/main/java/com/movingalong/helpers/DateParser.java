package com.movingalong.helpers;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateParser {
    public static LocalDate parseDateString(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            return LocalDate.parse(dateString, formatter);
        } catch (Exception e) {
            return null;
        }
    }

    public static LocalDate convertToDateToLocalDate(Date date) {
        Instant instant = date.toInstant();

        // Convert Instant to LocalDate using the system default time zone
        LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();

        return localDate;
    }
}
