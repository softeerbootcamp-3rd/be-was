package handler;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import logger.CustomLogger;

public class ResponseHandler {

    private ResponseHandler() {}

    private static class SingletonHelper {
        private static final ResponseHandler SINGLETON = new ResponseHandler();
    }

    public static ResponseHandler getInstance(){
        return SingletonHelper.SINGLETON;
    }

    public void process(OutputStream out, byte[] body) {
        DataOutputStream dos = new DataOutputStream(out);

        response200Header(dos, body.length);
        responseBody(dos, body);
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
