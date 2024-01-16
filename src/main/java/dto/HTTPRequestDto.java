package dto;

public class HTTPRequestDto {

    private String HTTP_Method;
    private String request_target;
    private String HTTP_version;
    private String host;
    private String accept;

    public HTTPRequestDto(String HTTP_Method, String request_target, String HTTP_version, String host, String accept) {
        this.HTTP_Method = HTTP_Method;
        this.request_target = request_target;
        this.HTTP_version = HTTP_version;
        this.host = host;
        this.accept = accept;
    }
    public HTTPRequestDto() {}

    public String getHTTP_Method() {
        return this.HTTP_Method;
    }
    public String getRequest_target() {
        return this.request_target;
    }
    public String getHTTP_version() {
        return this.HTTP_version;
    }
    public String getHost() {
        return this.host;
    }
    public String getAccept() {
        return this.accept;
    }

    public void setHTTP_Method(String HTTP_Method) {
        this.HTTP_Method = HTTP_Method;
    }
    public void setRequest_target(String request_target) {
        this.request_target = request_target;
    }
    public void setHTTP_version(String HTTP_version) {
        this.HTTP_version = HTTP_version;
    }
    public void setHost(String host) {
        this.host = host;
    }
    public void setAccept(String accept) {
        this.accept = accept;
    }

}
