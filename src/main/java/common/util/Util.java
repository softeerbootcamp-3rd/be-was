package common.util;

import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Util {

    private static final Charset UTF_8 = StandardCharsets.UTF_8;

    public static String[] split(String input, String delimiter) {
        return input.split(delimiter);
    }

    public static String decode(String input) {
        return URLDecoder.decode(input, UTF_8);
    }
}
