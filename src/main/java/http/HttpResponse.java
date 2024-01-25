package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HttpResponse {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private DataOutputStream dos;
    private HttpStatus httpStatus;
    private Map<String, String> headers = new HashMap<>();
    private byte[] body;

    private String path;

    public void setPath(String path) {
        this.path = path;
    }

    public HttpResponse(DataOutputStream dos) {
        this.dos = dos;
    }

    public void setHeader(String header, String content) {
        headers.put(header, content);
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getPath() {
        return path;
    }

    public void sendResponse() throws IOException {
        dos.writeBytes("HTTP/1.1 " + httpStatus.getCode() + " " + httpStatus.getMessage() + "\r\n");
        Set<String> keySet = headers.keySet();
        for (String key : keySet) {
            if (key.equals("Content-Type")) {
                dos.writeBytes(key+": "+getMimeTypeFromPath(path) +";charset=utf-8\r\n");
            } else {
                dos.writeBytes(key + ": " + headers.get(key) + "\r\n");
            }
        }
        dos.writeBytes("\r\n");
        readFile();
        if (body != null) {
            dos.write(body, 0, body.length);
        }
        dos.flush();
    }

    private String getMimeTypeFromPath(String path) {
        int periodIndex = path.lastIndexOf('.');
        String mime = path.substring(periodIndex+1).toUpperCase();
        return MimeType.valueOf(mime).getValue();
    }

    private void readFile() throws IOException {
        File file = new File(path);
        if (file != null && file.exists()) {
            FileInputStream fis = new FileInputStream(file);
            body = new byte[(int) file.length()];
            fis.read(body);
            fis.close();
        }
    }
}

