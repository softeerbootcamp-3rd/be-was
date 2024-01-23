package httpmessage.Request;

public class HttpRequest {

    private RequestHeader requestHeader;

    public HttpRequest(HttpMesssageReader httpMesssageReader){
        this.requestHeader = httpMesssageReader.getRequestHeader();
    }

    public void setPath(String path){
        this.requestHeader.setPath(path);
    }
    public String getPath(){
        return this.requestHeader.getPath();
    }
    public String getHttpMethod() {
        return this.requestHeader.getHttpMethod();
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
