package util;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class UrlParser {
    public static Map<String, String> parseQueryString(String queryString) {
        String decodedQuery = decodeQuery(queryString);
        Map<String, String> queryParams = new HashMap<>();
        for (String param : decodedQuery.split("&")) {
            String[] keyValue = param.split("=");
            if (keyValue.length == 2) {
                queryParams.put(keyValue[0], keyValue[1]);
            }
        }
        return queryParams;
    }

    private static String decodeQuery(String query){
        return URLDecoder.decode(query, StandardCharsets.UTF_8);
    }
}
