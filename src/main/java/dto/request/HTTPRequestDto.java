package dto.request;

import java.util.HashMap;
import java.util.Map;

public class HTTPRequestDto {

    private String HTTPMethod;
    private String requestTarget;
    private String HTTPVersion;
    private FirstClassCollection header;
    private FirstClassCollection requestParams;
    private FirstClassCollection body;            // http request body

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
        String length = header.getValue("Content-Length");
        if(length == null)
            return null;
        return Integer.parseInt(length);
    }
    public String getAccept() {
        return header.getValue("Accept");
    }
    public Map<String, String> getHeader() {
        return this.header.getMap();
    }

    public Map<String, String> getRequestParams() {
        return this.requestParams.getMap();
    }
    public Map<String, String> getBody() {
        return this.body.getMap();
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
    public void setBody(Map<String, String> body) {
        this.body = new FirstClassCollection(body);
    }
    public void setRequestParam(Map<String, String> requestParam) {
        this.requestParams = new FirstClassCollection(requestParam);
    }
    public void setHeader(Map<String, String> header) {
        this.header = new FirstClassCollection(header);
    }

    // 요청에 쿠키 값으로 세션 아이디가 포함되어 있다면 해당 세션 아이디 반환, 없으면 null 반환
    public String getSessionId() {
        String cookieValue = header.getValue("Cookie");
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
