package utils;

import java.io.DataOutputStream;
import java.io.IOException;

public class ResponseBuilder {

    public static void response200Header(DataOutputStream dos, int lengthOfBodyContent,
            String urn) throws IOException {
        dos.writeBytes("HTTP/1.1 200 OK \r\n");
        dos.writeBytes("Content-Type: " + contentType(urn) + ";charset=utf-8\r\n");
        dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
        dos.writeBytes("\r\n");
    }

    public static void responseBody(DataOutputStream dos, byte[] body) throws IOException {
        dos.write(body, 0, body.length);
        dos.flush();
    }

    private static String contentType(String urn) {
        if (urn.endsWith(".css")) {
            return "text/css";
        }
        if (urn.endsWith(".js")) {
            return "text/javascript";
        }
        if (urn.endsWith(".ico")) {
            return "image/x-icon";
        }
        if (urn.endsWith(".ttf")) {
            return "font/ttf";
        }
        if (urn.endsWith(".woff")) {
            return "font/woff";
        }
        return "text/html";
    }
}
