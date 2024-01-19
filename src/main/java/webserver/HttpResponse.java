package webserver;

import dto.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;

public class HttpResponse {

    // 로그 찍을 때 어떤 클래스인지 표시하는 용도
    private static final Logger logger = LoggerFactory.getLogger(HttpResponse.class);

    public static void response(DataOutputStream dos, Response response) throws IOException {
        HttpStatus httpStatus = response.getHttpStatus();
        StringBuilder sb = new StringBuilder();

        String line = "HTTP/1.1 " + response.getHttpStatus().getStatusCode() + " " + response.getHttpStatus().getStatusMessage();
        dos.writeBytes(line + "\r\n");
        sb.append(line);

        if (httpStatus == HttpStatus.OK) {
            sb.append(response200Header(dos, response));
            dos.writeBytes("\r\n");
            responseBody(dos, response);
        } else if (httpStatus == HttpStatus.FOUND) {
            sb.append(redirect(dos, response.getBody()));
        }

        dos.writeBytes("\r\n");
        dos.flush();
        dos.close();

        logger.debug("Response [" + sb + "]");
    }

    private static String response200Header(DataOutputStream dos, Response response) {
        try {
            String s1 = "Content-Type: " + response.getContentType();
            String s2 = "Content-Length: " + response.getBody().length;

            dos.writeBytes(s1 + "\r\n");
            dos.writeBytes(s2 + "\r\n");

            return ", " + s1 + ", " + s2;
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return null;
    }

    private static void responseBody(DataOutputStream dos, Response response) {
        try {
            dos.write(response.getBody(), 0, response.getBody().length);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

    }

    private static String redirect(DataOutputStream dos, byte[] body) {
        try {
            String redirectUrl = new String(body);
            String s = "Location: " + redirectUrl;

            dos.writeBytes(s + "\r\n");

            return s;
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return null;
    }


}
