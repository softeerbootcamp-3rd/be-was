package util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringParser {

    public static Map<String, String> parseQueryString(String query) throws UnsupportedEncodingException {
        if (!query.contains("\\?"))
            return null;

        String[] uri = query.split("\\?");
        Map<String, String> paramsMap = new HashMap<>();
        if (uri.length > 1) {
            paramsMap = StringParser.parseKeyValue(uri[1]);
        }

        return paramsMap;
    }

    public static Map<String, String> parseKeyValue(String query) throws UnsupportedEncodingException {
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
        if (cookieHeader == null)
            return null;

        String patternString = cookieName + "=([^;]+)";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(cookieHeader);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }

    public static String extractFileExtension(String filePath) {
        if (filePath == null || filePath.isEmpty()) return "";

        int lastDotIndex = filePath.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == filePath.length() - 1) return "";

        return filePath.substring(lastDotIndex + 1);
    }

}
