package httpmessage.response;

public class HttpResponse {
    private byte[] body;
    Integer statusCode;
    String statusLine;
    String contentType;
    String redirectPath;
    String sid = null;

    public void setHttpResponse(byte[] body, String contentType, Integer statusCode, String statusLine){
        this.body = body;
        this.statusCode = statusCode;
        this.statusLine = statusLine;
        this.contentType = contentType;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public void setPath(String path) {
        this.redirectPath = path;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public void setStatusLine(String line) {
        this.statusLine = line;
    }
    public byte[] getBody() {
        return body;
    }

    public String getContentType() {
        return contentType;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public String getStatusLine() {
        return statusLine;
    }

    public String getPath() { return this.redirectPath; }

    public String getSid() {
        return this.sid;
    }
}