package webserver;

import java.util.Map;

public class HttpRequest {
    private String method;
    private String path;
    private String protocolVersion;
    private Map<String, String> headers;

    private String body;

    public HttpRequest(String method, String path, String protocolVersion, Map<String, String> headers, String body) {
        this.method = method;
        this.path = path;
        this.protocolVersion = protocolVersion;
        this.headers = headers;
        this.body = body;
    }

    public String getMethod(){
        return method;
    }

    public String getPath(){
        return path;
    }

    public String getProtocolVersion(){
        return protocolVersion;
    }

    public Map<String, String> getHeaders(){
        return headers;
    }

    public String getBody(){
        return body;
    }

}