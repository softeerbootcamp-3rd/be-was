package util;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

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
}