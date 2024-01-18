package model;

public class Response {
    private String statusCode;
    private byte[] body;
    private String mimeType;
    private String redirectUrl;

    public String getStatusCode() {return this.statusCode;}
    public byte[] getBody() {return this.body;}
    public String getMimeType() {return this.mimeType;}
    public String getRedirectUrl() {return this.redirectUrl;}
    public void setStatusCode(String statusCode) {this.statusCode = statusCode;}
    public void setBody(byte[] body) {this.body = body;}
    public void setMimeType(String mimeType) {this.mimeType = mimeType;}

    public Response() {}
    public Response(String statusCode, byte[] body) {
        this.statusCode = statusCode;
        this.body = body;
    }
    public Response(String statusCode, String mimeType) {
        this.statusCode = statusCode;
        this.mimeType = mimeType;
    }
    public Response(String statusCode, byte[] body, String mimeType, String redirectUrl) {
        this.statusCode = statusCode;
        this.body = body;
        this.mimeType = mimeType;
        this.redirectUrl = redirectUrl;
    }
}
