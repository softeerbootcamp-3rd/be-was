package util;

import model.Request;
import model.Response;

import java.io.DataOutputStream;
import java.io.IOException;
import static model.MimeType.getContentType;

public class HttpResponse {
    public static void responseRedirectWithoutBody(DataOutputStream dos, Request request, Response response) throws IOException {
        dos.writeBytes(request.getVersion() + " " + response.getStatus().getStatusCode() + " " + response.getStatus().getMessage() + "\r\n");
        dos.writeBytes("Location: " + response.getRedirectUrl() + "\r\n");
        dos.writeBytes("\r\n");
        dos.flush();
    }

    public static void responseOKWithBody(DataOutputStream dos, Request request, Response response) throws IOException {
        String contentType = getContentType(request.getFilePath());

        dos.writeBytes(request.getVersion() + " " + response.getStatus().getStatusCode() + " " + response.getStatus().getMessage() + "\r\n");
        dos.writeBytes("Content-Type: " + contentType + ";charset=utf-8\r\n");
        dos.writeBytes("Content-Length: " + response.getBody().length + "\r\n");
        dos.writeBytes("\r\n");
        dos.write(response.getBody(), 0, response.getBody().length);
        dos.flush();
    }

    public static void responseWithoutBody(DataOutputStream dos, Request request, Response response) throws IOException {
        dos.writeBytes(request.getVersion() + " " + response.getStatus().getStatusCode() + " " + response.getStatus().getMessage() + "\r\n");
        dos.writeBytes("\r\n");
        dos.flush();
    }

}
