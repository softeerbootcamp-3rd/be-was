package model.http.response;

import model.http.ContentType;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class ResponseHeaders {
    private final LocalDateTime Date;
    private final ContentType contentType;
    private final String charSet;
    private final Integer contentLength;

    public ResponseHeaders(ContentType contentType, Integer contentLength) {
        Date = LocalDateTime.now();
        this.contentType = contentType;
        this.contentLength = contentLength;
        this.charSet = "utf-8";
    }
    public String getContentTypeHeader() {
        return "Content-Type: " + contentType.getType() + ";" + charSet + " \r\n";
    }

    public ContentType getContentType() {
        return contentType;
    }

    public String getCharSet() {
        return charSet;
    }

    public Integer getContentLength() {
        return contentLength;
    }

    public String getContentLengthHeader() {
        return "Content-Length: " + contentLength + "\r\n";
    }
}
