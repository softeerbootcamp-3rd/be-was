package util;

import db.Database;
import model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ResponseBuilder {
    private static final Logger logger = LoggerFactory.getLogger(ResponseBuilder.class);

    public static void sendResponse(DataOutputStream dos, byte[] body, String path, HttpStatus httpStatus, String extension) throws IOException {
        try {
            sendHeader(dos, httpStatus, path, body.length, extension);
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public static void sendHeader(DataOutputStream dos, HttpStatus httpStatus, String path, int bodyLength, String extension) throws IOException {
        dos.writeBytes("HTTP/1.1  " + httpStatus.getCode() + " " + httpStatus.getMessage() + "\r\n");
        if (httpStatus == HttpStatus.REDIRECT) {
            dos.writeBytes("Location: http://localhost:8080" + path + "\r\n");
        }
        dos.writeBytes("Content-Type: text/" + extension + ";charset=utf-8\r\n");
        dos.writeBytes("Content-Length: " + bodyLength + "\r\n");
        Model.getAttribute("sessionId").ifPresent(
                value -> {
                    String stringValue = String.valueOf(value);
                    try {
                        dos.writeBytes("Set-Cookie: sid=" + stringValue + "; Expires="
                                + formattingDate(Database.getSession(stringValue).getExpires()) + "; Path=/\r\n");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
        dos.writeBytes("\r\n");
    }

    private static String formattingDate(LocalDateTime date){
        DateTimeFormatter dataFormat = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss zz", Locale.ENGLISH);
        return date.atZone(ZoneId.of("Asia/Seoul")).format(dataFormat);
    }
}
