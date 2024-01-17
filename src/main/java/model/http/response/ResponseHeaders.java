package model.http.response;

import model.http.ContentType;

import java.time.LocalDateTime;

public class ResponseHeaders {
    private final LocalDateTime Date;
    private final ContentType contentType;
    private final String charSet;
    private final Integer contentLength;

    public ResponseHeaders(ContentType contentType, Integer contentLength, String charSet) {
        Date = LocalDateTime.now();
        this.contentType = contentType;
        this.contentLength = contentLength;
        this.charSet = charSet;
    }
    public String getContentTypeHeader() {
        return "Content-Type: " + contentType.getType() + ";" + charSet + " \r\n";
    }
    public String getContentLengthHeader() {
        return "Content-Length: " + contentLength + "\r\n";
    }
}