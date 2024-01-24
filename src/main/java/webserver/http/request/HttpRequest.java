package webserver.http.request;

import webserver.http.request.enums.HttpMethod;

import java.util.Map;

public class HttpRequest {
    private HttpMethod method;
    private String path;
    private String protocolVersion;
    private Map<String, String> queryParams;
    private Map<String, String> headers;
    private Map<String, String> body;

    public HttpRequest(HttpMethod method, String path, String protocolVersion, Map<String, String> queryParams, Map<String, String> headers, Map<String, String> body) {
        this.method = method;
        this.path = path;
        this.protocolVersion = protocolVersion;
        this.queryParams = queryParams;
        this.headers = headers;
        this.body = body;
    }

    public HttpMethod getMethod(){
        return method;
    }

    public String getPath(){
        return path;
    }

    public Map<String, String> getQueryParams(){
        return queryParams;
    }

    public String getProtocolVersion(){
        return protocolVersion;
    }

    public Map<String, String> getHeaders(){
        return headers;
    }

    public Map<String, String> getBody(){
        return body;
    }

}
