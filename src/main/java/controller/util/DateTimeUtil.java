package controller.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateTimeUtil {
    public static String pattern = "EEE, d MMM yyyy HH:mm:ss z";
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern, Locale.ENGLISH);
    public static String getGMTDateString(LocalDateTime localDate) {
        return formatter.format(localDate.atZone(ZoneId.of("GMT")));
    }

    public static LocalDateTime parseGMTDateString(String dateString) {
        return LocalDateTime.parse(dateString, formatter);
    }
}
