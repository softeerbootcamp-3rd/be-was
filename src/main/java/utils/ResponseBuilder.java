package utils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ResponseBuilder {

    public static void build302(DataOutputStream dos, String url) throws IOException {
        dos.writeBytes("HTTP/1.1 302 Found \r\n");
        dos.writeBytes("Location: " + url + "\r\n");
        dos.writeBytes("\r\n");
    }

    public static void build200(DataOutputStream dos, String url) throws IOException {
        // content
        byte[] body = Files.readAllBytes(new File(url).toPath());

        // header
        dos.writeBytes("HTTP/1.1 200 OK \r\n");
        dos.writeBytes("Content-Type: " + contentType(url) + ";charset=utf-8\r\n");
        dos.writeBytes("Content-Length: " + body.length + "\r\n");
        dos.writeBytes("\r\n");

        // body
        responseBody(dos, body);
    }

    private static void responseBody(DataOutputStream dos, byte[] body) throws IOException {
        dos.write(body, 0, body.length);
        dos.flush();
    }

    private static String contentType(String url) {
        if (url.endsWith(".css")) {
            return "text/css";
        }
        if (url.endsWith(".js")) {
            return "text/javascript";
        }
        if (url.endsWith(".ico")) {
            return "image/x-icon";
        }
        if (url.endsWith(".ttf")) {
            return "font/ttf";
        }
        if (url.endsWith(".woff")) {
            return "font/woff";
        }
        return "text/html";
    }
}
