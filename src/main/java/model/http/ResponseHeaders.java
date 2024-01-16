package model.http;

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

    public ResponseHeaders(Integer contentLength) {
        Date = LocalDateTime.now();
        this.contentType = ContentType.HTML;
        this.charSet = "uft-8";;
        this.contentLength = contentLength;
    }

    public LocalDateTime getDate() {
        return Date;
    }

    public String getContentType() {
        return "Content-Type: " + contentType.getType() + ";" + charSet + " \r\n";
    }

    public String getContentLength() {
        return "Content-Length: " + contentLength + "\r\n";
    }
}
