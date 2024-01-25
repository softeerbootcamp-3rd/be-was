package util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringParser {

    public static Map<String, String> parseKeyVaue(String query) throws UnsupportedEncodingException {
        Map<String, String> parseMap = new HashMap<>();
        for (String set: query.split("&")) {
            String[] keyValue = set.split("=");
            String key = URLDecoder.decode(keyValue[0], "UTF-8");
            String value = null;
            if (keyValue.length > 1) {
                value = URLDecoder.decode(keyValue[1], "UTF-8");
            }
            parseMap.put(key, value);
        }
        return parseMap;
    }

    public static String getCookieValue(String cookieHeader, String cookieName) {
        String patternString = cookieName + "=([^;]+)";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(cookieHeader);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }

}
