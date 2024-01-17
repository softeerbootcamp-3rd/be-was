package model;

public class Response {
    private final int statusCode;
    private final byte[] body;

    public Response(int statusCode, byte[] body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public byte[] getBody() {
        return body;
    }
}
