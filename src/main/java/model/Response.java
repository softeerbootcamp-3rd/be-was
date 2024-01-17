package model;

public class Response {
    private final int statusCode;
    private final byte[] body;
    private final String redirectUrl;

    public Response(int statusCode, byte[] body) {
        this.statusCode = statusCode;
        this.body = body;
        this.redirectUrl = null;
    }

    public Response(int statusCode, byte[] body, String redirectUrl) {
        this.statusCode = statusCode;
        this.body = body;
        this.redirectUrl = redirectUrl;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public byte[] getBody() {
        return body;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }
}

