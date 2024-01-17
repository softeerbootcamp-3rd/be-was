package util;

import dto.ResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;

public class ResponseBuilder {

    // 로그 찍을 때 어떤 클래스인지 표시하는 용도
    private static final Logger logger = LoggerFactory.getLogger(ResponseBuilder.class);

    public static void response(DataOutputStream dos, ResponseDto responseDto) {
        HttpStatus httpStatus = responseDto.getHttpStatus();
        if (httpStatus == HttpStatus.OK) {
            response200Header(dos, responseDto);
            responseBody(dos, responseDto);
        } else if (httpStatus == HttpStatus.FOUND) {
            redirect(dos, responseDto.getBody().toString());
        } else if (httpStatus == HttpStatus.NOT_FOUND) {
            response404Header(dos);
        } else if (httpStatus == HttpStatus.INTERNAL_SERVER_ERROR) {
            response500Header(dos);
        }
    }

    public static void response(DataOutputStream dos, HttpStatus httpStatus) {
        if (httpStatus == HttpStatus.INTERNAL_SERVER_ERROR)
            response500Header(dos);
    }

    private static void response200Header(DataOutputStream dos, ResponseDto responseDto) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + responseDto.getContentType() + "\r\n");
            dos.writeBytes("Content-Length: " + responseDto.getBody().length+ "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private static void responseBody(DataOutputStream dos, ResponseDto responseDto) {
        try {
            dos.write(responseDto.getBody(), 0, responseDto.getBody().length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private static void redirect(DataOutputStream dos, String redirectUrl) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found\r\n");
            dos.writeBytes("Location: " + redirectUrl + "\r\n");
            dos.writeBytes("\r\n");
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private static void response404Header(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 404 Not_Found\r\n");
            dos.writeBytes("\r\n");
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private static void response500Header(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 500 Internal_Server_Error\r\n");
            dos.writeBytes("\r\n");
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }


}
