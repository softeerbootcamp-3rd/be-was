package httpmessage.response;

import httpmessage.HttpStatusCode;

public class HttpResponse {
    private byte[] body;

    HttpStatusCode httpStatusCode;
    String contentType;
    String redirectionPath;
    String sid = null;
    String expireDate;

    public void setHttpResponse(byte[] body, String contentType, HttpStatusCode httpStatusCode){
        this.body = body;
        this.httpStatusCode = httpStatusCode;
        this.contentType = contentType;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public void setRedirectionPath(String path) {
        this.redirectionPath = path;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public void setHttpStatusCode(HttpStatusCode httpStatusCode) { this.httpStatusCode = httpStatusCode; }

    public String getExpireDate() {
        return expireDate;
    }

    public byte[] getBody() {
        return body;
    }

    public String getContentType() {
        return contentType;
    }

    public HttpStatusCode getHttpStatusCode() {
        return httpStatusCode;
    }

    public int getStatusCode() {
        return httpStatusCode.getValue();
    }
    public String getStatusLine() {
        return httpStatusCode.getReasonPhrase();
    }

    public String getPath() { return this.redirectionPath; }

    public String getSid() {
        return this.sid;
    }
}