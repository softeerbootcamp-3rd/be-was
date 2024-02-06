package model;

import java.io.*;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import static constant.HttpResponseConstant.*;
import static model.HttpStatus.FOUND;
import static model.HttpStatus.OK;

public class HttpResponse {
    private final HttpStatus httpStatus;
    private final Map<String, String> header;
    private byte[] body;

    public HttpResponse(HttpStatus httpStatus, Map<String, String> header, byte[] body) {
        this.httpStatus = httpStatus;
        this.header = header;
        this.body = body;
    }
    public static HttpResponse redirect(String location) {
        Map<String, String> header = new HashMap<>();
        header.put(LOCATION, location+CRLF);

        return new HttpResponse(FOUND, header, NO_BODY);
    }

    public static HttpResponse errorResponse(HttpStatus httpStatus, String errorMessage) {
        try {
            byte[] errorMessageBytes = errorMessage.getBytes("UTF-8");
            Map<String, String> header = new HashMap<>();

            header.put(CONTENT_TYPE, "text/plain;charset=utf-8" + CRLF);
            header.put(CONTENT_LENGTH, errorMessageBytes.length + CRLF);

            return new HttpResponse(httpStatus, header, errorMessageBytes);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static HttpResponse response200(String extension, String path) throws IOException {
        File file = new File(ResponseEnum.getPathName(extension) + path);
        byte[] body = new byte[(int) file.length()];

        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            fileInputStream.read(body);

            Map<String, String> header = new HashMap<>();
            header.put(CONTENT_TYPE, ResponseEnum.getContentType(extension) + ";charset=utf-8" + CRLF);
            header.put(CONTENT_LENGTH, body.length + CRLF);

            return new HttpResponse(OK, header, body);
        }
    }
    public void setBody(String target, String bodyInfo) throws UnsupportedEncodingException {
        String currentBody = new String(this.body);
        byte[] replaceBody = currentBody.replace(target, URLDecoder.decode(bodyInfo, "UTF-8")).getBytes();
        header.put(CONTENT_LENGTH, replaceBody.length + CRLF);//새로운 바디의 content_length로 바꾸기
        this.body = replaceBody;
    }

    public void addCookie(String cookieHeader) {
        this.header.put(COOKIE, cookieHeader);
    }

    public String getStartLine() {
        return VERSION + httpStatus.getCode() + " " + httpStatus.getStatus() + " " + CRLF;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public byte[] getBody() {
        return body;
    }

    @Override
    public String toString() {
        try {
            return "HttpResponse{" +
                    "httpStatus=" + httpStatus +
                    ", header=" + header +
                    ", body=" + new String(body, "UTF-8") +
                    '}';
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
