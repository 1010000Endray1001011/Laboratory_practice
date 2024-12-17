package ru.restaurant.util;

import lombok.experimental.UtilityClass;
import java.time.LocalDateTime;
import java.time.Duration;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class TimeUtils {
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(FORMATTER);
    }

    public static LocalDateTime parseDateTime(String dateTime) {
        return LocalDateTime.parse(dateTime, FORMATTER);
    }

    public static Duration calculateDuration(LocalDateTime start, LocalDateTime end) {
        return Duration.between(start, end);
    }

    public static LocalDateTime estimateCompletionTime(double operationTime) {
        return LocalDateTime.now().plusMinutes((long) (operationTime * 60));
    }
}