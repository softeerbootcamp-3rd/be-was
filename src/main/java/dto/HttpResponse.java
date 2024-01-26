package dto;

import http.Status;
import http.ContentType;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {

    private Status status;
    private final Map<String, String> headers = new HashMap<>();
    private byte[] body;

    private static final String RESOURCES_PATH = "src/main/resources/";

    public HttpResponse makeRedirect(String redirectUrl) {
        this.status = Status.REDIRECT;
        addHeader("Location", redirectUrl);
        return this;
    }

    public void makeBody(Status status, String path) throws IOException {
        String contentType = getContentType(path);
        this.status = status;
        this.body = getBody(path, contentType);
        addHeader("Content-Type", contentType);
        addHeader("Content-Length", String.valueOf(body.length));
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    private byte[] getBody(String url, String contentType) throws IOException {
        // 파일 경로 판단
        String path = RESOURCES_PATH;
        if (contentType.equals("text/html")) {
            path += "templates";
        } else {
            path += "static";
        }

        // 파일을 바이트 포맷으로 변환
        try (FileInputStream fileInputStream = new FileInputStream(path + url)) {
            return fileInputStream.readAllBytes();
        }
    }

    public Status getStatus() {
        return status;
    }

    public Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }

    public byte[] getBody() {
        return body;
    }

    public static String getContentType(String path) {
        String extension = getFileExtension(path);
        return ContentType.getMimeType(extension);
    }

    private static String getFileExtension(String path) {
        File file = new File(path);
        String name = file.getName();
        return name.substring(name.lastIndexOf(".") + 1);
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                "status=" + status +
                ", headers=" + headers +
                ", body=" + Arrays.toString(body) +
                '}';
    }
}
