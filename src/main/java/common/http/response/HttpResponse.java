package common.http.response;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {

    private HttpResponseStartLine startLine;
    private HttpResponseHeader header;
    private byte[] body;

    public HttpResponse(HttpResponseStartLine startLine, HttpResponseHeader header, byte[] body) {
        this.startLine = startLine;
        this.header = header;
        this.body = body;
    }

    public HttpResponseStartLine getStartLine() {
        return startLine;
    }

    public void setStartLine(HttpStatusCode httpStatusCode) {
        this.startLine = new HttpResponseStartLine(httpStatusCode);
    }

    public HttpResponseHeader getHeader() {
        return header;
    }

    public void setHeader(Map<String, String> headers) {
        this.header = new HttpResponseHeader(headers);
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public byte[] convertResponseToByteArray() {
        byte[] startLine = this.startLine.toString().getBytes();
        byte[] header = this.header.toString().getBytes();
        byte[] body = this.body;

        byte[] response = new byte[startLine.length + header.length + body.length];

        System.arraycopy(startLine, 0, response, 0, startLine.length);
        System.arraycopy(header, 0, response, startLine.length, header.length);
        System.arraycopy(body, 0, response, startLine.length + header.length, body.length);

        return response;
    }

    public static HttpResponse responseBuilder(HttpStatusCode httpStatusCode, HashMap<String, String> headers, byte[] body) {
        return new HttpResponse(
            new HttpResponseStartLine(httpStatusCode),
            new HttpResponseHeader(headers),
            body
        );
    }

}
