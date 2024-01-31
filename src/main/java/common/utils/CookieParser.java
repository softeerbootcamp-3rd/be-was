package common.utils;

import common.http.request.HttpRequest;
import java.util.Optional;

public class CookieParser {
    public static Optional<String> parseSidFromHeader(HttpRequest httpRequest) {
        String cookie = httpRequest.getHttpRequestHeader().getSpecificHeader("Cookie");
        String[] parts = cookie.split("; ");
        String sidValue = null;

        for (String part : parts) {
            if (part.startsWith("sid=")) {
                sidValue = part.substring(4);
                break;
            }
        }

        return Optional.ofNullable(sidValue);
    }
}
