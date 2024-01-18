package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ResponseBuilder {

    private static final Logger logger = LoggerFactory.getLogger(ResponseBuilder.class);

    public static void buildResponse(OutputStream out, String StatusCodeUrl) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);

        String[] tokens = StatusCodeUrl.split(" ");
        String statusCode = tokens[0];
        String targetPath = tokens[1];

        byte[] body = ResourceLoader.loadResource(targetPath);

        if(statusCode.equals("200")) {
            response200Header(dos, body.length);
        } else if(statusCode.equals("302")) {
            response302Header(dos, targetPath);
        } else if (statusCode.equals("404")) {
            response404Header(dos);
        } else {
            logger.error("지원하지 않는 상태 코드입니다.");
            return;
        }
        responseBody(dos, body);
    }
    private static void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private static void response302Header(DataOutputStream dos, String redirectLocation) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found\r\n");
            dos.writeBytes("Location: " + redirectLocation + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private static void response404Header(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 404 Not Found\r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private static void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
