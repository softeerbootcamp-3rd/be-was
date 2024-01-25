package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import response.HttpResponse;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public final class ViewResolver {

    private static final Logger logger = LoggerFactory.getLogger(ViewResolver.class);

    public static void response(HttpResponse httpResponse, OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);

        if (httpResponse.getStatusCode() == 200) {
            response200Header(dos, httpResponse.getBodyLength(), httpResponse.getContentType());
            responseBody(dos, httpResponse.getBody());
            return;
        }
        if (httpResponse.getStatusCode() == 302) {
            response302Header(dos, httpResponse.getRedirectUri());
            return;
        }
        if (httpResponse.getStatusCode() == 404) {
            response404Header(dos);
            responseBody(dos, httpResponse.getBody());
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
