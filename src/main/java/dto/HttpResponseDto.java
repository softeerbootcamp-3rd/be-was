package dto;

import model.http.ContentType;
import model.http.Header;
import model.http.Status;

import java.util.HashMap;

public class HttpResponseDto {
    public static final String HTTP_VERSION = "HTTP/1.1";
    public static final String UTF_8 = "utf-8";
    private final String version;
    private Status status;
    private ContentType contentType;
    private final String charSet;
    private Integer contentLength;
    private final HashMap<String, String> optionHeader;
    private byte[] content;

    public HttpResponseDto() {
        this.version = HTTP_VERSION;
        this.charSet = UTF_8;
        this.status = Status.OK;
        this.optionHeader = new HashMap<>();
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

    public Integer getContentLength() {
        return contentLength;
    }

    public void addHeader(String header, String content) {
        optionHeader.put(header, content);
    }
    public byte[] getContent() {
        return content;
    }

    public HashMap<String, String> getOptionHeader() {
        return optionHeader;
    }

    public void setContent(byte[] content) {
        this.content = content;
        this.contentLength = content.length;
    }
}
