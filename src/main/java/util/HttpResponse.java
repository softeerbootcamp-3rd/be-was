package util;

public class HttpResponse {

    private final HttpStatus status;

    private final byte[] data;

    public HttpResponse(HttpStatus status, String data) {
        this.status = status;
        this.data = data.getBytes();
    }

    public HttpResponse(HttpStatus status, byte[] data) {
        this.status = status;
        this.data = data;
    }

    public HttpResponse(HttpStatus status) {
        this.status = status;
        this.data = status.getMessage().getBytes();
    }

    public HttpStatus getStatus() {
        return status;
    }

    public byte[] getData() {
        return data;
    }
}
