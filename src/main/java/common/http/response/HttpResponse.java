package common.http.response;

import common.logger.CustomLogger;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {

    private StartLine startLine;
    private Header header;
    private byte[] body;

    public HttpResponse(StartLine startLine, Header header, byte[] body) {
        this.startLine = startLine;
        this.header = header;
        this.body = body;
    }

    public StartLine getStartLine() {
        return startLine;
    }

    public void setStartLine(HttpStatusCode httpStatusCode) {
        this.startLine = new StartLine(httpStatusCode);
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Map<String, String> headers) {
        this.header = new Header(headers);
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

        int totalLength = startLine.length + header.length + body.length;

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(totalLength);

        try (byteArrayOutputStream) {
            byteArrayOutputStream.write(startLine);
            byteArrayOutputStream.write(header);
            byteArrayOutputStream.write(body);
        } catch (IOException e) {
            CustomLogger.printError(e);
        }
        return byteArrayOutputStream.toByteArray();
    }

    public static HttpResponse responseBuilder(HttpStatusCode httpStatusCode, HashMap<String, String> headers, byte[] body) {
        return new HttpResponse(
            new StartLine(httpStatusCode),
            new Header(headers),
            body
        );
    }

}
