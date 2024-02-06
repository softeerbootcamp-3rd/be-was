package dto;

import http.constants.Status;
import http.constants.ContentType;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static common.util.FileManager.getFile;

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
        this.body = getFile(path, contentType);
        addHeader("Content-Type", contentType);
        addHeader("Content-Length", String.valueOf(body.length));
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
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
