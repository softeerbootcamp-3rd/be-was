package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import controller.UserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileUtil;
import webserver.http.request.HttpMethod;
import webserver.http.request.HttpRequest;
import webserver.http.request.HttpRequestParser;
import webserver.http.response.HttpResponse;
import webserver.http.response.HttpResponseBuilder;
import webserver.http.response.HttpResponseSender;
import webserver.http.response.HttpStatus;
import webserver.routing.DynamicRoutingManager;
import webserver.routing.StaticRoutingManager;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private static final byte[] METHOD_NOT_FOUND = "Method Not Found".getBytes(StandardCharsets.UTF_8);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            HttpRequest httpRequest = new HttpRequestParser().parse(in);
            requestLogging(httpRequest);

            DataOutputStream dos = new DataOutputStream(out);
            HttpResponse httpResponse;

            // GET 메소드
            if (httpRequest.getMethod() == HttpMethod.GET){
                // 정적 로직 라우팅
                httpResponse = StaticRoutingManager.handleRequest(httpRequest);
                // 동적 로직 라우팅
                if(httpResponse == null){
                    httpResponse = DynamicRoutingManager.handleRequest(httpRequest);
                }
            } else {
                httpResponse = new HttpResponseBuilder().createErrorResponse(HttpStatus.BAD_REQUEST, METHOD_NOT_FOUND);
            }

            new HttpResponseSender().sendResponse(httpResponse, dos);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void requestLogging(HttpRequest httpRequest){
        StringBuilder logMessage = new StringBuilder();

        logMessage.append("\n========== HTTP Request ==========\n");
        logMessage.append("Method: ").append(httpRequest.getMethod()).append("\n");
        logMessage.append("Path: ").append(httpRequest.getPath()).append("\n");
        if (httpRequest.getQueryParams() != null) {
            httpRequest.getQueryParams().forEach((key, value) ->
                    logMessage.append("QueryParam: ").append(key).append(" = ").append(value).append("\n"));
        }
        logMessage.append("Protocol Version: ").append(httpRequest.getProtocolVersion()).append("\n");
        if (httpRequest.getHeaders() != null) {
            httpRequest.getHeaders().forEach((key, value) ->
                    logMessage.append("Header: ").append(key).append(" = ").append(value).append("\n"));
        }
        if (!httpRequest.getBody().isEmpty()) {
            logMessage.append("Body: ").append(httpRequest.getBody()).append("\n");
        }
        logMessage.append("==================================");

        logger.debug(logMessage.toString());
    }

}
