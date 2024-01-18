package model;

public class Response {
    private HttpStatus status;
    private byte[] body;
    private String redirectUrl;

    public Response(HttpStatus status, byte[] body) {
        this.status = status;
        this.body = body;
    }

    public Response(HttpStatus status) {
        this.status = status;
        this.redirectUrl = "/";
    }

    public Response(HttpStatus status, String redirectUrl) {
        this.status = status;
        this.redirectUrl = redirectUrl;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public byte[] getBody() {
        return body;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }
}
