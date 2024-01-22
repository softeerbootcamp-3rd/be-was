package webserver;

import common.response.Status;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static common.response.Status.REDIRECT;

public class Response {

    private static final String RESOURCES_PATH = "src/main/resources/";

    public static void createResponse(OutputStream out, Status status, String contentType, String url) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        byte[] body = Files.readAllBytes(new File(getFilePath(url)).toPath());
        if (status == REDIRECT) {
            response302Header(dos, url);
            return;
        }
        responseHeader(dos, status, contentType, body.length);
        responseBody(dos, body);
    }

    private static void responseHeader(DataOutputStream dos, Status status, String contentType, int lengthOfBodyContent) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String statusLine = stringBuilder.append("HTTP/1.1 ")
                .append(status.getCode())
                .append(" ")
                .append(status.getMsg())
                .append("\r\n")
                .toString();

        dos.writeBytes(statusLine);
        dos.writeBytes("Content-Type: " + contentType + "\r\n");
        dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
        dos.writeBytes("\r\n");
    }

    private static void response302Header(DataOutputStream dos, String location) throws IOException {
        dos.writeBytes("HTTP/1.1 302 Found \r\n");
        dos.writeBytes("Location: " + location);
        dos.writeBytes("\r\n");
    }

    private static String getFilePath(String url) {
        String path = RESOURCES_PATH;
        if (url.startsWith("/css") || url.startsWith("/fonts") || url.startsWith("/images") || url.startsWith("/js") || url.equals("/favicon.ico")) {
            path += "static";
        } else {
            path += "templates";
        }
        return path + url;
    }

    private static void responseBody(DataOutputStream dos, byte[] body) throws IOException {
        dos.write(body, 0, body.length);
        dos.flush();
    }

    public static String getMimeType(String path) throws IOException {
        Path source = Paths.get(path);
        return Files.probeContentType(source);
    }
}
