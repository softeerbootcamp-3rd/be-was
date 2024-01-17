package webserver;

import config.AppConfig;
import dto.HttpResponseDto;
import factory.HttpRequestFactory;
import factory.HttpResponseFactory;
import model.http.request.HttpRequest;
import model.http.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.DynamicResponseBuilder;
import service.HttpResponseSender;
import service.StaticResponseBuilder;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final List<String> staticElements = List.of("/user/create?");
    private final Socket connection;
    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }
    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader inBufferedReader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            HttpResponseFactory httpResponseFactory = AppConfig.httpResponseFactory();
            HttpResponseSender httpResponseService = AppConfig.httpResponseService();
            HttpRequestFactory httpRequestFactory = AppConfig.httpRequestFactory();
            StaticResponseBuilder staticResponseBuilder = AppConfig.staticResponseBuilder();
            DynamicResponseBuilder dynamicResponseBuilder = AppConfig.dynamicResponseBuilder();
            HttpRequest httpRequest = httpRequestFactory.create(inBufferedReader);
            HttpResponseDto httpResponseDto = new HttpResponseDto();
            logger.debug(httpRequest.toString());

            boolean isDynamic = staticElements.stream().anyMatch(httpRequest.getStartLine().getPathUrl()::startsWith);
            if (isDynamic) {
                logger.debug("동적인 response 전달");
                dynamicResponseBuilder.build(httpRequest, httpResponseDto);
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