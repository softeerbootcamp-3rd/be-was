package common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Random;

public class Util {

    private static final Random random = new Random();
    private static final String UTF_8 = "utf-8";

    public static String decode(String input) throws UnsupportedEncodingException {
        return URLDecoder.decode(input, UTF_8);
    }

    public static String getRandomString() {  //10자리 랜덤 문자열 생성
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int sessionIdLength = 10;

        return random.ints(leftLimit, rightLimit + 1)
                .limit(sessionIdLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
