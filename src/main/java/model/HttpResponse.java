package model;

import webserver.HttpStatus;
import webserver.ResponseEnum;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import static constant.HttpRequestConstant.*;

public class HttpResponse {
    private static final String VERSION = "HTTP/1.1 ";
    private static final String CONTENT_TYPE = "Content-Type: ";
    private static final String CONTENT_LENGTH = "Content-Length: ";
    private static final String LOCATION = "Location: ";
    private static final String ERROR = "Error: ";
    private static final byte[] NO_BODY = "".getBytes();
    private final HttpStatus httpStatus;
    private Map<String, String> header;
    private byte[] body;

    public HttpResponse(HttpStatus httpStatus, Map<String, String> header, byte[] body) {
        this.httpStatus = httpStatus;
        this.header = header;
        this.body = body;
    }

    public void send(OutputStream out) {
        String startLine =  VERSION + httpStatus.getCode() + " " + httpStatus.getStatus() + " \r\n";

        try (DataOutputStream dos = new DataOutputStream(out)){
            dos.writeBytes(startLine);
            for (Map.Entry<String, String> header : header.entrySet()) {
                dos.writeBytes(header.getKey() + header.getValue());
            }
            dos.writeBytes("\r\n");

            if(body == NO_BODY){
                return;
            }
            try {
                dos.write(body, 0, body.length);
                dos.flush();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static HttpResponse redirect(HttpStatus httpStatus, String location) {
        Map<String, String> header = new HashMap<>();
        header.put(LOCATION, location);

        return new HttpResponse(httpStatus, header, NO_BODY);
    }

    public static HttpResponse response400(HttpStatus httpStatus, String errorMessage) {
        Map<String, String> header = new HashMap<>();
        header.put(CONTENT_TYPE, "text/plain;charset=utf-8\r\n");
        header.put(ERROR, errorMessage);

        return new HttpResponse(httpStatus, header, NO_BODY);
    }

    public static HttpResponse response200(HttpRequest httpRequest, HttpStatus httpStatus) throws IOException {
        String path = httpRequest.getUri().getPath();
        String extension = path.split(EXTENSION_DELIMITER)[EXTENSION_POS];

        byte[] body = Files.readAllBytes(new File(ResponseEnum.getPathName(extension) + path).toPath());

        Map<String, String> header = new HashMap<>();

        header.put(CONTENT_TYPE, ResponseEnum.getContentType(extension)+";charset=utf-8\r\n");
        header.put(CONTENT_LENGTH, String.valueOf(body.length) + "\r\n");

        return new HttpResponse(httpStatus, header, body);
    }

}
