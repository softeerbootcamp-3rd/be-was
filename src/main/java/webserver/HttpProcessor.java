package webserver;

import handler.ResponseHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import logger.CustomLogger;
import logger.HttpRequestHeader;

public class HttpProcessor implements Runnable {

    private final Socket connection;

    public HttpProcessor(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // HTTP Request Header 읽기
            HttpRequestHeader httpRequestHeader = HttpRequestHeader.httpRequestHeaderBuilder(in);

            // HTTP Request Header 및 IP,Port 출력하기
            CustomLogger.printIPAndPort(connection);
            CustomLogger.printHeader(httpRequestHeader);

            // HTTP Request Handler
            // TODO: HTTP Request Handler 구현

            // HTTP Response Handler
            ResponseHandler responseHandler = ResponseHandler.initBuilder(true, out, httpRequestHeader.getPath());
            responseHandler.process();
        } catch (IOException e) {
            CustomLogger.printError(e);
        }
    }
}
