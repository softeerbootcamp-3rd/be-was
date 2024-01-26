package config;

import java.util.Arrays;

public class HTTPResponse {
    private String HTTPType;
    private int code;
    private String status;
    private String contentType;
    private byte[] head;
    private byte[] body;

    public HTTPResponse(String HTTPType, int code, String status, byte[] head, byte[] body) {
        this.HTTPType = HTTPType;
        this.code = code;
        this.status = status;
        this.head = head;
        this.body = body;
    }

    public HTTPResponse(String HTTPType, int code, String status) {
        this.HTTPType = HTTPType;
        this.code = code;
        this.status = status;
        this.head = ("HTTP/1.1 " + code +" "+ status +"\r\n"+
                "Content-Length: " + 0  + "\r\n").getBytes();;
        this.body = new byte[0];
    }

    public byte[] getHead() {
        return head;
    }

    public byte[] getBody() {
        return body;
    }



    @Override
    public String toString() {
        return "HTTPResponse{" +
                "HTTPType='" + HTTPType + '\'' +
                ", code=" + code +
                ", status='" + status + '\'' +
                ", contentType='" + contentType + '\'' +
                ", head=" + Arrays.toString(head) +
                ", body=" + Arrays.toString(body) +
                '}';
    }
}
