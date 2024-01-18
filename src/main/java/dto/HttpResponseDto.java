package dto;

import model.http.ContentType;
import model.http.Status;

public class HttpResponseDto {
    public static final String HTTP_VERSION = "HTTP/1.1";
    public static final String UTF_8 = "utf-8";
    private final String version;
    private Status status;
    private ContentType contentType;
    private final String charSet;
    private Integer contentLength;
    private String location;
    private byte[] content;

    public HttpResponseDto() {
        this.version = HTTP_VERSION;
        this.charSet = UTF_8;
        this.status = Status.OK;
    }

    public String getVersion() {
        return version;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public String getCharSet() {
        return charSet;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getContentLength() {
        return contentLength;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
        this.contentLength = content.length;
    }
}