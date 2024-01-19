package model.http.response;

import model.http.ContentType;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;

public class ResponseHeaders {
    private final Date date;
    private final ContentType contentType;
    private final String charSet;
    private final Integer contentLength;
    private final HashMap<String, String> optionHeader;

    public ResponseHeaders(ContentType contentType, Integer contentLength, String charSet) {
        this.date = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        this.contentType = contentType;
        this.charSet = charSet;
        this.contentLength = contentLength;
        this.optionHeader = new HashMap<>();
    }

    public HashMap<String, String> getOptionHeader() {
        return optionHeader;
    }

    public String getDate() {
        return "Date: " + date + "\r\n";
    }

    public String getContentTypeHeader() {
        return "Content-Type: " + contentType.getType() + ";" + charSet + " \r\n";
    }

    public String getContentLengthHeader() {
        return "Content-Length: " + contentLength + "\r\n";
    }

    public void addOptionHeader(String header, String content) {
        optionHeader.put(header, content);
    }
}
