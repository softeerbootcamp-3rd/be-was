package webserver;

import config.AppConfig;
import factory.HttpRequestFactory;
import factory.HttpResponseFactory;
import model.http.Status;
import model.http.request.HttpRequest;
import model.http.response.HttpResponse;
import dto.HttpResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.HttpResponseSender;
import service.StaticResponseBuilder;
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
            HttpResponseSender httpResponseService = appConfig.httpResponseService();
            HttpRequestFactory httpRequestFactory = appConfig.httpRequestFactory();
            StaticResponseBuilder staticResponseBuilder = appConfig.staticResponseBuilder();

            HttpRequest httpRequest = httpRequestFactory.create(inBufferedReader); // httpRequest init 생성
            HttpResponseDto httpResponseDto = new HttpResponseDto();
            logger.debug(httpRequest.toString());

            boolean isDynamic = staticElements.stream().anyMatch(httpRequest.getStartLine().getPathUrl()::startsWith);
            if (isDynamic) {
                logger.debug("동적인 response 전달");
                Servlet servlet = new Servlet();
            } else {
                logger.debug("정적인 response 전달");
                staticResponseBuilder.build(httpRequest, httpResponseDto);
            }
            //응답을 보내는 부분
            HttpResponse httpResponse = httpResponseFactory.create(httpResponseDto);
            httpResponseService.sendHttpResponse(out, httpResponse);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}