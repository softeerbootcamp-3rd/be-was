package dto.request;

import java.util.HashMap;

public class HTTPRequestDto {

    private String HTTPMethod;
    private String requestTarget;
    private String HTTPVersion;
    private HashMap<String, String> header;
    private HashMap<String, String> requestParams;
    private String body;            // http request body

    public HTTPRequestDto(String HTTPMethod, String requestTarget, String HTTPVersion, String body) {
        this.HTTPMethod = HTTPMethod;
        this.requestTarget = requestTarget;
        this.HTTPVersion = HTTPVersion;
        this.header = new HashMap<>();
        this.requestParams = new HashMap<>();       // 쿼리 스트링이 들어왔을 경우 저장
        this.body = body;
    }
    public HTTPRequestDto() {
        this.requestParams = new HashMap<>();
        this.header = new HashMap<>();
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
    public Integer getContentLength() {
        String length = header.get("Content-Length");
        if(length == null)
            return null;
        return Integer.parseInt(length);
    }
    public String getAccept() {
        return header.get("Accept");
    }
    public HashMap<String, String> getHeader() {
        return this.header;
    }

    public HashMap<String, String> getRequestParams() {
        return this.requestParams;
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
    public void setBody(String body) {
        this.body = body;
    }
    public void addRequestParam(String key, String value) {
        this.requestParams.put(key, value);
    }
    public void addHeader(String key, String value) {
        header.put(key, value);
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

    // 요청에 쿠키 값으로 세션 아이디가 포함되어 있다면 해당 세션 아이디 반환, 없으면 null 반환
    public String getSessionId() {
        String cookieValue = header.get("Cookie");
        if(cookieValue == null)
            return null;
        if(cookieValue.contains("sid")) {
            // session id 추출
            String sessionId = cookieValue.substring("sid=".length());
            if (sessionId.contains(";"))
                sessionId = sessionId.substring(0, cookieValue.indexOf(";"));
            return sessionId;
        }
        return null;
    }
}
