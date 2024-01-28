package dto;

import constant.HttpHeader;

import java.util.Map;

public class HttpHeaders {
    private Map<HttpHeader, String> headers;

    public HttpHeaders(Map<HttpHeader, String> headers) {
        this.headers = headers;
    }

    public String get(HttpHeader headerName) {
        return headers.get(headerName);
    }

    public String buildResponseHeader() {
        StringBuilder stringBuilder = new StringBuilder();
        headers.forEach((key, value) -> {
            stringBuilder.append(key.getHeaderName()).append(": ")
                    .append(value).append("\r\n");
        });
        return stringBuilder.toString();
    }
}
