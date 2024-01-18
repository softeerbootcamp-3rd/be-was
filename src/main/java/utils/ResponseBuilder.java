package utils;

import dto.HttpResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * 응답 반환
 */
public class ResponseBuilder {
    private static final Logger logger = LoggerFactory.getLogger(ResponseBuilder.class);

    public static void send(DataOutputStream dos, HttpResponseDto response) {
        HttpStatus status = response.getStatus();
        try {
            dos.writeBytes("HTTP/1.1 " + status + "\r\n");
            dos.writeBytes("Content-Type: " + response.getContent().getType() + ";charset=utf-8\r\n");

            byte[] body = response.getBody();
            dos.writeBytes("Content-Length: " + body.length + "\r\n");
            dos.writeBytes("\r\n");
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

}
