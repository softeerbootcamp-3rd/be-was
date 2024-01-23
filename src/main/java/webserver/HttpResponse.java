package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.io.IOException;

public class HttpResponse {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private DataOutputStream dos;

    public HttpResponse(DataOutputStream dos) {
        this.dos = dos;
    }

    public void response200Header(String path) throws IOException {

        byte[] body = null;
        File file = new File(path);
        if (file != null && file.exists()) {
            FileInputStream fis = new FileInputStream(file);
            body = new byte[(int) file.length()];
            fis.read(body);
            fis.close();
        }

        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: "+ getMimeTypeFromPath(path) +";charset=utf-8\r\n");
            if(body != null)
                dos.writeBytes("Content-Length: " + body.length+ "\r\n");
            dos.writeBytes("\r\n");
            responseBody(body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public void response302RedirectHeader(String redirectUrl) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found\r\n");
            dos.writeBytes("Location: " + redirectUrl + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public void responseBody(byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public void respond404() {
        try {
            String response = "HTTP/1.1 404 Not Found\r\n\r\n";
            dos.writeBytes(response);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private String getMimeTypeFromPath(String path) {
        int periodIndex = path.lastIndexOf('.');
        String mime = path.substring(periodIndex+1).toUpperCase();
        return MimeType.valueOf(mime).getValue();
    }
}
