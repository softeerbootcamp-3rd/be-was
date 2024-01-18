package model.http.response;

import model.http.ContentType;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class ResponseHeaders {
    private final Date date;
    private final ContentType contentType;
    private final String charSet;
    private final Integer contentLength;
    private String location;

    public ResponseHeaders(ContentType contentType, Integer contentLength, String charSet) {
        this.date = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        this.contentType = contentType;
        this.charSet = charSet;
        this.contentLength = contentLength;
    }

    public String getDate() {
        return "Date: " + date + "\r\n";
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContentTypeHeader() {
        return "Content-Type: " + contentType.getType() + ";" + charSet + " \r\n";
    }

    public String getContentLengthHeader() {
        return "Content-Length: " + contentLength + "\r\n";
    }

    public String getLocationTypeHeader() {
        return "Location: " + location + "\r\n";
    }
}