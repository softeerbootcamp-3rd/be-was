package webserver;

import config.AppConfig;
import dto.HttpResponseDto;
import factory.HttpRequestFactory;
import factory.HttpResponseFactory;
import handler.DynamicResponseHandler;
import handler.StaticResponseHandler;
import model.http.request.HttpRequest;
import model.http.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.HttpResponseSendService;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final List<String> dynamicElements = List.of("/user/create?");
    private final Socket connection;
    private final HttpResponseFactory httpResponseFactory;
    private final HttpResponseSendService httpResponseSendService;
    private final HttpRequestFactory httpRequestFactory;
    private final StaticResponseHandler staticResponseHandler;
    private final DynamicResponseHandler dynamicResponseBuilder;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        this.httpResponseFactory = AppConfig.httpResponseFactory();
        this.httpResponseSendService = AppConfig.httpResponseSendService();
        this.httpRequestFactory = AppConfig.httpRequestFactory();
        this.staticResponseHandler = AppConfig.staticResponseBuilder();
        this.dynamicResponseBuilder = AppConfig.dynamicResponseBuilder();
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {

            BufferedReader inBufferedReader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

            HttpRequest httpRequest = httpRequestFactory.create(inBufferedReader);
            HttpResponseDto httpResponseDto = new HttpResponseDto();

            logger.debug(httpRequest.toString());

            boolean isDynamic = dynamicElements.stream().anyMatch(httpRequest.getStartLine().getPathUrl()::startsWith);
            if (isDynamic) {
                logger.debug("동적인 response 전달");
                dynamicResponseBuilder.handle(httpRequest, httpResponseDto);
            } else {
                logger.debug("정적인 response 전달");
                staticResponseHandler.handle(httpRequest, httpResponseDto);
            }

            HttpResponse httpResponse = httpResponseFactory.create(httpResponseDto);
            httpResponseSendService.sendHttpResponse(out, httpResponse);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}