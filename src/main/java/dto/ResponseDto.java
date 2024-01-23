package dto;

import constant.ErrorCode;
import constant.FileContentType;
import constant.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class ResponseDto {
    private HttpStatus status = null;
    private List<String> headers = new ArrayList<>();
    private byte[] body = null;

    public void makeError(ErrorCode errorCode) {
        this.status = errorCode.getHttpStatus();
        this.body = errorCode.getErrorMessage().getBytes();
        addContentType(FileContentType.TXT.getContentType());
    }

    public ResponseDto makeRedirect(String redirectUrl) {
        this.status = HttpStatus.REDIRECT;
        addHeader("Location", redirectUrl);
        addContentType(FileContentType.HTML.getContentType());
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
