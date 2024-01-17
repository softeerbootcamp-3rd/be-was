package util;

import model.Request;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static model.MimeType.getContentType;

public class HttpResponse {


    public static void responseRedirectHeader(DataOutputStream dos, String redirectUrl) throws IOException {
        dos.writeBytes("HTTP/1.1 302 Found\r\n");
        dos.writeBytes("Location: " + redirectUrl + "\r\n");
        dos.writeBytes("\r\n");

    }

    public static void response200Header(int lengthOfBodyContent, Request request, DataOutputStream dos) throws IOException {
        String contentType = getContentType(request.getFilePath());
        dos.writeBytes("HTTP/1.1 200 OK \r\n");
        dos.writeBytes("Content-Type: " + contentType + ";charset=utf-8\r\n");
        dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
        dos.writeBytes("\r\n");
    }

    public static void responseBadRequest(DataOutputStream dos, String message) throws IOException {
        sendResponseWithMessage(dos, "HTTP/1.1 400 Bad Request", message);
    }

    public static void responseInternalServerError(DataOutputStream dos, String message) throws IOException {
        sendResponseWithMessage(dos, "HTTP/1.1 500 Internal Server Error", message);
    }

    private static void sendResponseWithMessage(DataOutputStream dos, String statusLine, String message) throws IOException {
        dos.writeBytes(statusLine + "\r\n");
        dos.writeBytes("Content-Type: text/html; charset=utf-8\r\n");
        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
        dos.writeBytes("Content-Length: " + messageBytes.length + "\r\n");
        dos.writeBytes("\r\n");
        dos.write(messageBytes);
    }

}
