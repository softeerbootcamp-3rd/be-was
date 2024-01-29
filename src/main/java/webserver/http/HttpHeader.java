package webserver.http;

import org.checkerframework.checker.units.qual.A;

import java.util.*;
import java.util.stream.Collectors;

public class HttpHeader {

    public Map<String, List<String>> headers;

    public static final String ACCEPT = "Accept";
    public static final String ACCEPT_ENCODING = "Accept-Encoding";
    public static final String ACCEPT_LANGUAGE = "Accept-Language";
    public static final String CONNECTION = "Connection";
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_DISPOSITION = "Content-Disposition";
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
        return headers.containsKey(CONTENT_TYPE) ? headers.get(CONTENT_TYPE).get(0) : null;
    }

    public String getContentLength() {
        return headers.containsKey(CONTENT_LENGTH) ? headers.get(CONTENT_LENGTH).get(0) : null;
    }

    public String getLocation() {
        return headers.containsKey(LOCATION) ? headers.get(LOCATION).get(0) : null;
    }

    public String getSetCookie() {
        return headers.get(SET_COOKIE)
                .stream()
                .collect(Collectors.joining("; "));
    }

    public String getCookie() {
        return headers.containsKey(COOKIE) ? headers.get(COOKIE).get(0) : null;
    }

    public String getHost() {
        return headers.containsKey(HOST) ? headers.get(HOST).get(0) : null;
    }

    public String getAccept() {
        return headers.containsKey(ACCEPT) ? headers.get(ACCEPT).get(0) : null;
    }

    public String getContentDisposition()  {
        return headers.containsKey(CONTENT_DISPOSITION) ? headers.get(CONTENT_DISPOSITION).get(0) : null;
    }

    public String getBoundary() {
        List<String> contentTypeValues = headers.get(HttpHeader.CONTENT_TYPE);
        if (contentTypeValues != null) {
            for (String contentType : contentTypeValues) {
                if (contentType.startsWith("multipart/form-data")) {
                    // boundary를 Content-Type 헤더에서 추출합니다.
                    String[] parts = contentType.split(";");
                    for (String part : parts) {
                        if (part.trim().startsWith("boundary=")) {
                            return part.trim().substring("boundary=".length());
                        }
                    }
                }
            }
        }
        return null;
    }

    public boolean isMultipart() {
        String contentType = getContentType();
        return contentType != null && contentType.startsWith("multipart/form-data");
    }

    public void setContentType(String contentType) {
        headers.put(CONTENT_TYPE, Collections.singletonList(contentType));
    }

    public void setContentLength(String contentLength) {
        headers.put(CONTENT_LENGTH, Collections.singletonList(contentLength));
    }

    public boolean hasSetCookie() {
        return headers.containsKey(SET_COOKIE) ? true : false;
    }

}
