package com.example.delivery.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReservationNumberGenerator {

    private static final String DATE_PATTERN = "yyMMdd";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);

    private ReservationNumberGenerator() {
    }

    public static String withDate(long deliveryCount) {
        LocalDateTime now = LocalDateTime.now();
        StringBuilder sb = new StringBuilder();

        return sb.append(now.format(FORMATTER))
                .append("-")
                .append(deliveryCount).toString();
    }

}
