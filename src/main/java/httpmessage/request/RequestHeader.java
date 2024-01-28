package httpmessage.request;

public class RequestHeader {
    private String httpMethod;
    private String path;
    private String accpet;
    private String acceptEncoding;
    private String acceptLanguage;
    private String upgradeInsecureRequests;

    private StringBuilder body;
    private String cookie = "";

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public void setAccpet(String accpet) {
        this.accpet = accpet;
    }
    public void setAcceptEncoding(String acceptEncoding) {
        this.acceptEncoding = acceptEncoding;
    }

    public void setAcceptLanguage(String acceptLanguage) {
        this.acceptLanguage = acceptLanguage;
    }

    public void setUpgradeInsecureRequests(String upgradeInsecureRequests) {
        this.upgradeInsecureRequests = upgradeInsecureRequests;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setBody(StringBuilder body) {
        this.body = body;
    }

    public StringBuilder getBody() {
        return this.body;
    }

    public String getPath(){
        return this.path;
    }
    public String getHttpMethod() {
        return this.httpMethod;
    }

    public String getAcceptEncoding() {
        return this.acceptEncoding;
    }


    public String getAcceptLanguage() {
        return this.acceptLanguage;
    }

    public String getAccpet() {
        return this.accpet;
    }

    public String getUpgradeInsecureRequests() {
        return this.upgradeInsecureRequests;
    }
    public String getCookie() {
        return cookie;
    }



}
