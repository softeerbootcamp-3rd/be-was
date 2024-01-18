package handler;

import http.request.HttpRequest;
import logger.CustomLogger;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ResponseHandler {

    private ResponseHandler() {}

    private static class SingletonHelper {
        private static final ResponseHandler SINGLETON = new ResponseHandler();
    }

    public static ResponseHandler getInstance(){
        return SingletonHelper.SINGLETON;
    }

    public void process(OutputStream out, byte[] body, HttpRequest httpRequest) {
        DataOutputStream dos = new DataOutputStream(out);

        if (httpRequest.getHttpRequestStartLine().getRequestTarget().lastIndexOf(".html") != -1)
            response200Header(dos, body.length, "text/html");
        else if (httpRequest.getHttpRequestStartLine().getRequestTarget().lastIndexOf(".css") != -1)
            response200Header(dos, body.length, "text/css");
        else if (httpRequest.getHttpRequestStartLine().getRequestTarget().lastIndexOf(".js") != -1)
            response200Header(dos, body.length,"application/javascript");
        else if (httpRequest.getHttpRequestStartLine().getRequestTarget().lastIndexOf(".png") != -1)
            response200Header(dos, body.length,"image/png");
        else if (httpRequest.getHttpRequestStartLine().getRequestTarget().lastIndexOf(".eot") != -1)
            response200Header(dos, body.length,"application/vnd.ms-fontobject");
        else if (httpRequest.getHttpRequestStartLine().getRequestTarget().lastIndexOf(".ttf") != -1)
            response200Header(dos, body.length,"application/font-sfnt");
        else if (httpRequest.getHttpRequestStartLine().getRequestTarget().lastIndexOf(".svg") != -1)
            response200Header(dos, body.length,"image/svg+xml");
        else if (httpRequest.getHttpRequestStartLine().getRequestTarget().lastIndexOf(".woff") != -1)
            response200Header(dos, body.length,"font/woff");
        else if (httpRequest.getHttpRequestStartLine().getRequestTarget().lastIndexOf(".woff2") != -1)
            response200Header(dos, body.length,"font/woff2");
        responseBody(dos, body);
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String contentType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType + ";charset=utf-8\r\n");
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
