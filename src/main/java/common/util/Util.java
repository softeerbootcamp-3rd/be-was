package common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class Util {

    private static final String UTF_8 = "utf-8";

    public static String[] split(String input, String delimiter) {
        return input.split(delimiter);
    }

    public static String decode(String input) throws UnsupportedEncodingException {
        return URLDecoder.decode(input, UTF_8);
    }
}
