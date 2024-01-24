package webserver;

import common.response.Status;
import dto.HttpRequest;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static common.response.Status.REDIRECT;
import static webserver.RequestHandler.logger;

public class Response {

    private final DataOutputStream dos;
    private final String contentType;
    private Status status;
    private String path;
    private Map<String, String> headers;
    private byte[] body;

    private static final String RESOURCES_PATH = "src/main/resources/";

    public Response(OutputStream out, HttpRequest request) {
        this.dos = new DataOutputStream(out);
        this.contentType = getContentType(request.getPath());
        this.headers = new HashMap<>();
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public void setHeader(String key, String value) {
        if (key.equals("Set-Cookie")) {
            value += "; path=/";
        }
        headers.put(key, value);
    }

    public void send() throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(getFilePath(path))) {
            setBody(fileInputStream.readAllBytes());
            if (status == REDIRECT) {
                responseHeader(path);
                return;
            }
            responseHeader();
            responseBody();
        } catch (FileNotFoundException e) {
            logger.debug(e.getMessage());
        }
    }

    private void responseHeader() throws IOException {
        dos.writeBytes("HTTP/1.1 " + status.getCode() + " " + status.getMsg() + "\r\n");
        dos.writeBytes("Content-Type: " + contentType + "\r\n");
        dos.writeBytes("Content-Length: " + body.length + "\r\n");
        writeAdditionalHeaders();
    }

    private void responseHeader(String location) throws IOException {
        dos.writeBytes("HTTP/1.1 " + status.getCode() + " " + status.getMsg() + "\r\n");
        dos.writeBytes("Location: " + location + "\r\n");
        writeAdditionalHeaders();
    }

    private void writeAdditionalHeaders() throws IOException {
        if (!headers.isEmpty()) {
            for (String key : headers.keySet()) {
                dos.writeBytes(key + ": " + headers.get(key) + "\r\n");
            }
        }
        dos.writeBytes("\r\n");
    }

    private void responseBody() throws IOException {
        dos.write(body, 0, body.length);
        dos.flush();
    }

    private String getFilePath(String url) {
        String path = RESOURCES_PATH;
        if (url.startsWith("/css") || url.startsWith("/fonts") || url.startsWith("/images") || url.startsWith("/js") || url.equals("/favicon.ico")) {
            path += "static";
        } else {
            path += "templates";
        }
        return path + url;
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
}
