package dto;

import java.util.HashMap;

public class HTTPRequestDto {

    private String HTTPMethod;
    private String requestTarget;
    private String HTTPVersion;
    private String host;
    private String accept;
    private HashMap<String, String> requestParams;
    private Integer contentLength;   // http request body의 길이
    private String body;            // http request body

    public HTTPRequestDto(String HTTPMethod, String requestTarget, String HTTPVersion, String host, String accept, Integer contentLength, String body) {
        this.HTTPMethod = HTTPMethod;
        this.requestTarget = requestTarget;
        this.HTTPVersion = HTTPVersion;
        this.host = host;
        this.accept = accept;
        this.requestParams = new HashMap<>();       // 쿼리 스트링이 들어왔을 경우 저장
        this.contentLength = contentLength;
        this.body = body;
    }
    public HTTPRequestDto() {
        this.requestParams = new HashMap<>();
    }

    public String getHTTPMethod() {
        return this.HTTPMethod;
    }
    public String getRequestTarget() {
        return this.requestTarget;
    }
    public String getHTTPVersion() {
        return this.HTTPVersion;
    }
    public String getHost() {
        return this.host;
    }
    public String getAccept() {
        return this.accept;
    }
    public HashMap<String, String> getRequestParams() {
        return this.requestParams;
    }
    public Integer getContentLength() {
        return this.contentLength;
    }
    public String getBody() {
        return this.body;
    }

    public void setHTTPMethod(String HTTP_Method) {
        this.HTTPMethod = HTTP_Method;
    }
    public void setRequestTarget(String request_target) {
        this.requestTarget = request_target;
    }
    public void setHTTPVersion(String HTTP_version) {
        this.HTTPVersion = HTTP_version;
    }
    public void setHost(String host) {
        this.host = host;
    }
    public void setAccept(String accept) {
        this.accept = accept;
    }
    public void setRequestParams(HashMap<String, String> requestParams) {
        this.requestParams = requestParams;
    }
    public void setContentLength(Integer contentLength) {
        this.contentLength = contentLength;
    }
    public void setBody(String body) {
        this.body = body;
    }
    public void addRequestParam(String key, String value) {
        this.requestParams.put(key, value);
    }

    public HashMap<String, String> bodyParsing() {
        HashMap<String, String> bodyMap = new HashMap<>();
        String[] tokens = body.split("&");
        for(int i = 0; i < tokens.length; i++) {
            String key = tokens[i].substring(0, tokens[i].indexOf("="));
            String value = tokens[i].substring(tokens[i].indexOf("=") + 1);
            bodyMap.put(key, value);
        }
        return bodyMap;
    }
}
