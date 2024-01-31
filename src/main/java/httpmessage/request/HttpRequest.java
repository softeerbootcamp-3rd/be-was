package httpmessage.request;

import java.util.Map;

public class HttpRequest {

    private RequestHeader requestHeader;
    private Parameter parameter;

    public HttpRequest(HttpMessageReader httpMesssageReader){
        this.requestHeader = httpMesssageReader.getRequestHeader();
        this.parameter = httpMesssageReader.getParameter();
    }

    public void setPath(String path){
        this.requestHeader.setPath(path);
    }
    public String getCookie(){return requestHeader.getCookie(); }
    public Map<String, String> getParmeter() {
        return parameter.getValues();
    }
    public String getParmeter(String key) {
        return parameter.getValue(key);
    }

    public String getPath(){
        return this.requestHeader.getPath();
    }
    public String getHttpMethod() {
        return this.requestHeader.getHttpMethod();
    }
    public StringBuilder getBody() {
        return this.requestHeader.getBody();
    }

    public String getAcceptEncoding() {
        return this.requestHeader.getAcceptEncoding();
    }


    public String getAcceptLanguage() {
        return this.requestHeader.getAcceptLanguage();
    }

    public String getAccpet() {
        return this.requestHeader.getAccpet();
    }

    public String getUpgradeInsecureRequests() {
        return this.requestHeader.getUpgradeInsecureRequests();
    }


}
