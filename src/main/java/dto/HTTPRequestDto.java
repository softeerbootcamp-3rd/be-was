package dto;

public class HTTPRequestDto {

    private String HTTPMethod;
    private String requestTarget;
    private String HTTPVersion;
    private String host;
    private String accept;

    public HTTPRequestDto(String HTTP_Method, String request_target, String HTTP_version, String host, String accept) {
        this.HTTPMethod = HTTP_Method;
        this.requestTarget = request_target;
        this.HTTPVersion = HTTP_version;
        this.host = host;
        this.accept = accept;
    }
    public HTTPRequestDto() {}

    public String getHTTP_Method() {
        return this.HTTPMethod;
    }
    public String getRequest_target() {
        return this.requestTarget;
    }
    public String getHTTP_version() {
        return this.HTTPVersion;
    }
    public String getHost() {
        return this.host;
    }
    public String getAccept() {
        return this.accept;
    }

    public void setHTTP_Method(String HTTP_Method) {
        this.HTTPMethod = HTTP_Method;
    }
    public void setRequest_target(String request_target) {
        this.requestTarget = request_target;
    }
    public void setHTTP_version(String HTTP_version) {
        this.HTTPVersion = HTTP_version;
    }
    public void setHost(String host) {
        this.host = host;
    }
    public void setAccept(String accept) {
        this.accept = accept;
    }

}
