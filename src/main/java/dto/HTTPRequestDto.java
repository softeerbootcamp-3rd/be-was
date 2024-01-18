package dto;

import java.util.HashMap;

public class HTTPRequestDto {

    private String HTTPMethod;
    private String requestTarget;
    private String HTTPVersion;
    private String host;
    private String accept;
    private HashMap<String, String> requestParams;

    public HTTPRequestDto(String HTTP_Method, String request_target, String HTTP_version, String host, String accept) {
        this.HTTPMethod = HTTP_Method;
        this.requestTarget = request_target;
        this.HTTPVersion = HTTP_version;
        this.host = host;
        this.accept = accept;
        this.requestParams = new HashMap<>();       // 쿼리 스트링이 들어왔을 경우 저장
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
    public void addRequestParam(String key, String value) {
        this.requestParams.put(key, value);
    }
}
