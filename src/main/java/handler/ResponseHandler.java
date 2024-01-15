package handler;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import logger.CustomLogger;

public class ResponseHandler {
    private boolean isStaticResource;
    private DataOutputStream dos;
    private String path;


    private ResponseHandler(boolean isStaticResource, OutputStream out, String path) {
        this.isStaticResource = isStaticResource;
        this.dos = new DataOutputStream(out);
        this.path = path;
    }

    public static ResponseHandler initBuilder(boolean isStaticResource, OutputStream out, String path) {
        return new ResponseHandler(isStaticResource, out, path);
    }

    public void process() {
        if (isStaticResource) {
            staticResponse();
        } else {
            dynamicResponse();
        }
    }

    // 정적 처리
    private void staticResponse() {
        try {
            byte[] body = Files.readAllBytes(Paths.get("src/main/resources/templates" + this.path));
            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            CustomLogger.printError(e);
        }
    }

    // 동적 처리
    private void dynamicResponse() {
        // TODO: 동적 처리
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            CustomLogger.printError(e);
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            CustomLogger.printError(e);
        }
    }
}
