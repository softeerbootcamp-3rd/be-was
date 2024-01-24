package util;

public class StringUtils {
    public static boolean hasLength(String str) {
        return (str != null && !str.isEmpty());
    }
    public static boolean hasText(String str) {
        return (str != null && !str.isBlank());
    }
}