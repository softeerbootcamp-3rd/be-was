package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import response.HttpResponse;
import util.StatusCode;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public final class ViewResolver {

    private static final Logger logger = LoggerFactory.getLogger(ViewResolver.class);

    public static void response(StatusCode status, String path, OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);

        if (status.getStatus() == 200) {
            HttpResponse httpResponse = new HttpResponse(path);
            response200Header(dos, httpResponse.getBodyLength(), httpResponse.getContentType());
            responseBody(dos, httpResponse.getBody());
        }
        if (status.getStatus() == 302) {
            HttpResponse httpResponse = new HttpResponse(path, "/index.html");
            response302Header(dos, httpResponse.getRedirectUrl());
        }
        if (status.getStatus() == 404) {
            response404Header(dos);
        }
    }


    private static void response200Header(DataOutputStream dos, int lengthOfBodyContent, String contentType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private static void response302Header(DataOutputStream dos, String location) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + location + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private static void response404Header(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 404 Not Found \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }


    private static void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush(); //
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
