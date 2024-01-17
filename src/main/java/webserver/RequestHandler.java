package webserver;

import config.AppConfig;
import factory.HttpRequestFactory;
import factory.HttpResponseFactory;
import model.http.Body;
import model.http.request.HttpRequest;
import model.http.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.HttpResponseService;
import service.WebServerFileService;
import webApplicationServer.Servlet;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final List<String> staticElements = List.of("/user/create?");
    private final Socket connection;
    private final AppConfig appConfig;
    public RequestHandler(Socket connectionSocket, AppConfig appConfig) {
        this.connection = connectionSocket;
        this.appConfig = appConfig;
    }
    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader inBufferedReader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            HttpResponseFactory httpResponseFactory = appConfig.httpResponseFactory();
            HttpResponseService httpResponseService = appConfig.httpResponseService();
            HttpRequestFactory httpRequestFactory = appConfig.httpRequestFactory();
            WebServerFileService webServerFileService = appConfig.webServerFileService();
            HttpRequest httpRequest = httpRequestFactory.create(inBufferedReader); // httpRequest init 생성

            byte[] body = null;
            logger.debug(httpRequest.toString());
            boolean isDynamic = staticElements.stream().anyMatch(httpRequest.getStartLine().getPathUrl()::startsWith);
            if (isDynamic) {
                logger.debug("동적인 response 전달");
                Servlet servlet = new Servlet();
            } else { // 동적인 상황 컨트롤
                logger.debug("정적인 response 전달");
                body = webServerFileService.getFile(httpRequest);
            }
            HttpResponse httpResponse = httpResponseFactory.create(httpRequest, body); // httpResponse init 생성
            httpResponseService.sendHttpResponse(out, httpResponse);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}