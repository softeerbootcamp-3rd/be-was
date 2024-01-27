package dto;

import constant.ErrorCode;
import constant.MimeType;
import constant.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class Response {
    private HttpStatus status;
    private List<String> headers;
    private byte[] body;

    public Response() {
        this.status = null;
        this.headers = new ArrayList<>();
        this.body = null;
    }

    public void makeError(ErrorCode errorCode) {
        this.status = errorCode.httpStatus;
        this.body = errorCode.errorMessage.getBytes();
        addContentType(MimeType.TXT.contentType);
    }

    public Response makeRedirect(String redirectUrl) {
        this.status = HttpStatus.REDIRECT;
        addHeader("Location", redirectUrl);
        addContentType(MimeType.HTML.contentType);
        return this;
    }

    public void makeBody(byte[] body, String contentType) {
        this.status = HttpStatus.OK;
        this.body = body;
        addHeader("Content-Length", String.valueOf(body.length));
        addContentType(contentType);
    }

    public void addContentType(String fileContentType) {
        addHeader("Content-Type", (fileContentType + ";charset=utf-8"));
    }

    public void addHeader(String key, String value) {
        this.headers.add(key + ": " + value + "\r\n");
    }

    public HttpStatus getStatus() {
        return status;
    }

    public byte[] getBody() {
        return body;
    }

    public List<String> getHeaders() {
        return headers;
    }
}
