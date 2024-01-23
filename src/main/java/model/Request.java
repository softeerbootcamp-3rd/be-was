package model;

import java.util.Map;

public class Request {
    private HttpMethod method;
    private String URI;
    private String httpVer;
    private Map<String, String> headerMap;
    private String body;
    private Map<String, String> paramMap;

    public Request() {
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

    public void setHeaderMap(Map<String, String> headerMap) {
        this.headerMap = headerMap;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setParamMap(Map<String, String> paramMap) {
        this.paramMap = paramMap;
    }

    public Map<String, String> getHeaderMap() {
        return headerMap;
    }

    public Map<String, String> getParamMap() {
        return paramMap;
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
        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
            builder.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        if (paramMap != null) {
            builder.append("====== Request Param ======\n");
            for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                builder.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
        }

        if (body != null) {
            builder.append("====== Request Body ======\n")
                    .append("Body = " + body);
        }

        return builder.toString();
    }
}
