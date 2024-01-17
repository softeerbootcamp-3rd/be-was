package dto;

import model.http.ContentType;
import model.http.Status;

public class HttpResponseDto {
    public static final String HTTP_VERSION = "HTTP/1.1";
    public static final String UTF_8 = "utf-8";
    private String version;
    private Status status;
    private ContentType contentType;
    private String charSet;
    private Integer contentLength;
    private byte[] content;

    public HttpResponseDto() {
        this.version = HTTP_VERSION;
        this.charSet = UTF_8;
        this.status = Status.OK;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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

    public void setCharSet(String charSet) {
        this.charSet = charSet;
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