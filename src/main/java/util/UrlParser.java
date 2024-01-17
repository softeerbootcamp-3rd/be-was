package util;

import java.util.HashMap;
import java.util.Map;

public class UrlParser {
    public static Map<String, String> parseQueryString(String queryString) {
        Map<String, String> queryParams = new HashMap<>();
        for (String param : queryString.split("&")) {
            String[] keyValue = param.split("=");
            if (keyValue.length == 2) {
                queryParams.put(keyValue[0], keyValue[1]);
            }
        }
        return queryParams;
    }
}
