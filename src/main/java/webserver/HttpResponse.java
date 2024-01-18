package webserver;

import dto.ResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;

public class HttpResponse {

    // 로그 찍을 때 어떤 클래스인지 표시하는 용도
    private static final Logger logger = LoggerFactory.getLogger(HttpResponse.class);

    public static void response(DataOutputStream dos, ResponseBuilder responseBuilder) throws IOException {
        HttpStatus httpStatus = responseBuilder.getHttpStatus();
        StringBuilder sb = new StringBuilder();

        if (httpStatus == HttpStatus.OK) {
            sb.append(response200Header(dos, responseBuilder));
            responseBody(dos, responseBuilder);
        } else if (httpStatus == HttpStatus.FOUND) {
            sb.append(redirect(dos, responseBuilder.getBody()));
        } else if (httpStatus == HttpStatus.NOT_FOUND) {
            sb.append(response404Header(dos));
        } else if (httpStatus == HttpStatus.INTERNAL_SERVER_ERROR) {
            sb.append(response500Header(dos));
        }

        dos.close();
        
        logger.debug("Response [" + sb + "]");
    }

    public static void response(DataOutputStream dos, HttpStatus httpStatus) {
        if (httpStatus == HttpStatus.INTERNAL_SERVER_ERROR)
            response500Header(dos);
    }

    private static String response200Header(DataOutputStream dos, ResponseBuilder responseBuilder) {
        try {
            String s1 = "HTTP/1.1 200 OK";
            String s2 = "Content-Type: " + responseBuilder.getContentType();
            String s3 = "Content-Length: " + responseBuilder.getBody().length;

            dos.writeBytes(s1+ " \r\n");
            dos.writeBytes(s2 + "\r\n");
            dos.writeBytes(s3 + "\r\n");
            dos.writeBytes("\r\n");

            return s1 + ", " + s2 + ", " + s3;
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return null;
    }

    private static String responseBody(DataOutputStream dos, ResponseBuilder responseBuilder) {
        try {
            dos.write(responseBuilder.getBody(), 0, responseBuilder.getBody().length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return null;
    }

    private static String redirect(DataOutputStream dos, byte[] body) {
        try {
            String redirectUrl = new String(body);
            String s1 = "HTTP/1.1 302 Found";
            String s2 = "Location: " + redirectUrl;

            dos.writeBytes(s1 + "\r\n");
            dos.writeBytes(s2 + "\r\n");
            dos.writeBytes("\r\n");
            dos.flush();

            return s1 + ", " + s2;
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return null;
    }

    private static String response404Header(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 404 Not_Found\r\n");
            dos.writeBytes("\r\n");
            dos.flush();

            return "HTTP/1.1 404 Not_Found";
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return null;
    }

    private static String response500Header(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 500 Internal_Server_Error\r\n");
            dos.writeBytes("\r\n");
            dos.flush();

            return "HTTP/1.1 500 Internal_Server_Error";
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return null;
    }


}
