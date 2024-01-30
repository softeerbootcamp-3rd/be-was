package model;

import constant.HttpMethod;

import java.util.Map;

public class HttpRequest {
    private HttpMethod method;
    private String URI;
    private String httpVer;
    private HttpHeader headerMap;
    private Parameter paramMap;
    private Cookie cookieMap;
    private String body;

    public HttpRequest() {
    }


    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public void setURI(String URI) {
        this.URI = URI;
    }

    public void setHttpVer(String httpVer) {
        this.httpVer = httpVer;
    }

    public void setHeaderMap(HttpHeader header) {
        this.headerMap = header;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setParamMap(Parameter paramMap) {
        this.paramMap = paramMap;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getURI() {
        return URI;
    }

    public String getHttpVer() {
        return httpVer;
    }

    public String getBody() {
        return body;
    }

    public String getParameter(String key){
        return paramMap.get(key);
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("\n")
                .append("====== Request Line ======\n")
                .append("HTTP Method = ").append(method.name()).append("\n")
                .append("URI = ").append(URI).append("\n")
                .append("Http version = ").append(httpVer).append("\n")
                .append("====== Request Header ======\n");
        for (Map.Entry<String, String> header : headerMap.getHeader().entrySet()) {
            builder.append(header.getKey()).append(": ").append(header.getValue()).append("\n");
        }

        if (paramMap != null) {
            builder.append("====== Request Param ======\n");
            for (Map.Entry<String, String> entry : paramMap.getParamMap().entrySet()) {
                builder.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
        }

        if (body != null) {
            builder.append("====== Request Body ======\n")
                    .append("Body = " + body);
        }

        return builder.toString();
    }

    public String getHeader(String key) {
        return headerMap.get(key);
    }

    public String getCookie(String key) {
        if (cookieMap == null) {
            initCookieMap();
        }

        if (cookieMap.getCookieMap().size() != 0) {
            String value = cookieMap.get(key);
            if (value != null) {
                return value;
            }
        }

        return "";
    }

    private void initCookieMap() {
        String cookieString = headerMap.get("Cookie");
        String[] cookies = cookieString.split("; ");

        Cookie cookieMap = new Cookie();
        for (String cookie : cookies) {
            String[] keyValue = cookie.split("=");
            cookieMap.put(keyValue[0], keyValue[1]);
        }

        this.cookieMap = cookieMap;
    }
}
