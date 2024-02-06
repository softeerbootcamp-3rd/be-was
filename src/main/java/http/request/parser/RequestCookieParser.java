package http.request.parser;

import java.util.HashMap;
import java.util.Map;

public class RequestCookieParser {
    public static Map<String, String> parseCookies(String cookieHeader) {
        Map<String, String> cookies = new HashMap<>();
        if (cookieHeader != null && !cookieHeader.isEmpty()) {
            String[] pairs = cookieHeader.split(";");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=", 2);
                if (keyValue.length == 2) {
                    cookies.put(keyValue[0].trim(), keyValue[1].trim());
                }
            }
        }
        return cookies;
    }
}
