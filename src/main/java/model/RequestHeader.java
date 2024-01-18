package model;

import service.ParsingService;

public class RequestHeader {
    private String generalHeader;
    private String httpMethod;
    private String path;
    private String accpet;
    private String acceptEncoding;
    private String acceptLanguage;
    private String upgradeInsecureRequests;


    public void setGeneralHeader(String generalHeader) {
        this.generalHeader = generalHeader;
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

    public String getPath(){
        return this.path;
    }
    public String getGeneralHeader() {
        return generalHeader;
    }
    public String getHttpMethod() {
        return httpMethod;
    }

    public String getAcceptEncoding() {
        return acceptEncoding;
    }


    public String getAcceptLanguage() {
        return acceptLanguage;
    }

    public String getAccpet() {
        return accpet;
    }

    public String getUpgradeInsecureRequests() {
        return upgradeInsecureRequests;
    }


}
