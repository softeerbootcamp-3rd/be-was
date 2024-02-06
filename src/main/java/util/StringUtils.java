package util;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    public static String decode(String input) {
        return URLDecoder.decode(input, StandardCharsets.UTF_8);
    }
    public static boolean hasLength(String str) {
        return (str != null && !str.isEmpty());
    }
    public static boolean hasText(String str) {
        return (str != null && !str.isBlank());
    }

    public static boolean isMatched(String str, String pattern) {
        Pattern regexPattern = Pattern.compile(pattern);

        Matcher matcher = regexPattern.matcher(str);

        return matcher.matches();
    }
}