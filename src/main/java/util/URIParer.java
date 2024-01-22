package util;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class URIParer {

    public static Map<String, String> parserKeyValue(String query) throws UnsupportedEncodingException {
        Map<String, String> parseMap = new HashMap<>();
        for (String set: query.split("&")) {
            String[] keyValue = set.split("=");
            String key = URLDecoder.decode(keyValue[0], "UTF-8");
            String value = URLDecoder.decode(keyValue[1], "UTF-8");
            parseMap.put(key, value);
        }
        return parseMap;
    }
}
