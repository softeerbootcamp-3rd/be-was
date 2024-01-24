package webserver.http;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class HttpHeader implements Serializable {

    public static Map<String, List<String>> headers = new HashMap<>();

    public static final String ACCEPT = "Accept";
    public static final String ACCEPT_ENCODING = "Accept-Encoding";
    public static final String ACCEPT_LANGUAGE = "Accept-Language";
    public static final String CONNECTION = "Connection";
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String COOKIE = "Cookie";
    public static final String HOST = "Host";
    public static final String LOCATION = "Location";
    public static final String ORIGIN = "Origin";
    public static final String REFERER = "Referer";
    public static final String SET_COOKIE = "Set-Cookie";
    public static final String USER_AGENT = "User-Agent";

    public HttpHeader(Map<String, List<String>> headers) {
        this.headers = headers;
    }

    @Override
    public String toString() {
        return formatHeaders(this.headers);
    }

    public static String formatHeaders(Map<String, List<String>> headers) {
        return headers.entrySet().stream()
                .map(entry -> {
                    List<String> values = entry.getValue();
                    return entry.getKey() + ":" + (values.size() == 1 ?
                            "\"" + values.get(0) + "\"" :
                            values.stream().map(s -> "\"" + s + "\"").collect(Collectors.joining(", ")));
                })
                .collect(Collectors.joining(", ", "[", "]"));
    }

    public String getContentType() {
        return headers.get(CONTENT_TYPE).get(0);
    }

    public void setContentType(String contentType) {
        headers.put(CONTENT_TYPE, Collections.singletonList(contentType));
    }


    public String getLocation() {
        return headers.containsKey(LOCATION) ? headers.get(LOCATION).get(0) : null;
    }

    public String getSetCookie() {
        return headers.get(SET_COOKIE)
                .stream()
                .collect(Collectors.joining("; "));
    }

    public boolean hasSetCookie() {
        return headers.containsKey(SET_COOKIE) ? true : false;
    }

}
