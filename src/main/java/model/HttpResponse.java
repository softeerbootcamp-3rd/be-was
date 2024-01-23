package model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static model.HttpStatus.FOUND;
import static model.HttpStatus.OK;

public class HttpResponse {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);
    private static final String VERSION = "HTTP/1.1 ";
    private static final String CONTENT_TYPE = "Content-Type: ";
    private static final String CONTENT_LENGTH = "Content-Length: ";
    private static final String LOCATION = "Location: ";
    private static final byte[] NO_BODY = "".getBytes();
    private static final String CRLF = "\r\n";

    private final HttpStatus httpStatus;
    private final Map<String, String> header;
    private final byte[] body;

    public HttpResponse(HttpStatus httpStatus, Map<String, String> header, byte[] body) {
        this.httpStatus = httpStatus;
        this.header = header;
        this.body = body;
    }

    public void send(OutputStream out) {
        String startLine =  VERSION + httpStatus.getCode() + " " + httpStatus.getStatus() + " " + CRLF;

        try (DataOutputStream dos = new DataOutputStream(out)){
            dos.writeBytes(startLine);
            for (Map.Entry<String, String> header : header.entrySet()) {
                dos.writeBytes(header.getKey() + header.getValue());
            }
            dos.writeBytes(CRLF);

            if(body == NO_BODY){
                return;
            }
            try {//body
                dos.write(body, 0, body.length);
                dos.flush();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static HttpResponse redirect(String location) {
        Map<String, String> header = new HashMap<>();
        header.put(LOCATION, location);

        return new HttpResponse(FOUND, header, NO_BODY);
    }

    public static HttpResponse errorResponse(HttpStatus httpStatus, String errorMessage) {
        byte[] errorMessageBytes;

        try {
            errorMessageBytes = errorMessage.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        Map<String, String> header = new HashMap<>();
        header.put(CONTENT_TYPE, "text/plain;charset=utf-8" + CRLF);
        header.put(CONTENT_LENGTH, errorMessageBytes.length + CRLF);

        return new HttpResponse(httpStatus, header, errorMessageBytes);
    }

    public static HttpResponse response200(String extension, String path) throws IOException {
        File file = new File(ResponseEnum.getPathName(extension) + path);
        byte[] body = new byte[(int) file.length()];

        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            fileInputStream.read(body);

            Map<String, String> header = new HashMap<>();
            header.put(CONTENT_TYPE, ResponseEnum.getContentType(extension) + ";charset=utf-8" + CRLF);
            header.put(CONTENT_LENGTH, body.length + CRLF);

            return new HttpResponse(OK, header, body);
        }
    }

    @Override
    public String toString() {
        try {
            return "HttpResponse{" +
                    "httpStatus=" + httpStatus +
                    ", body=" + new String(body, "UTF-8") +
                    '}';
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
