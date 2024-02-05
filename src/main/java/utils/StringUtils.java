package utils;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class StringUtils {
    private static final String NEWLINE = System.getProperty("line.separator");

    private StringUtils() {
    }

    public static String appendNewLine(String str){
        return str + NEWLINE;
    }

    public static String decode(String str){
        return URLDecoder.decode(str, StandardCharsets.UTF_8);
    }
}
