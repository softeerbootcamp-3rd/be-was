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
    public LocalDateTime getDate() {
        return Date;
    }

    public String getContentType() {
        return contentType + ";" + charSet;
    }

    public String getCharSet() {
        return charSet;
    }

    public Integer getContentLength() {
        return contentLength;
    }
}
