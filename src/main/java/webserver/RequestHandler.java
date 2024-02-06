package webserver;

import dto.HttpResponseDto;
import factory.HttpRequestFactory;
import factory.HttpResponseFactory;
import filter.Filter;
import handler.DynamicResponseHandler;
import handler.StaticResponseHandler;
import model.http.request.HttpRequest;
import model.http.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.HttpResponseSendService;

import java.io.*;
import java.net.Socket;
import java.util.List;

import static config.AppConfig.*;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private final Socket connection;
    private final HttpResponseFactory httpResponseFactory;
    private final HttpResponseSendService httpResponseSendService;
    private final HttpRequestFactory httpRequestFactory;
    private final Filter filter;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        this.httpResponseFactory = httpResponseFactory();
        this.httpResponseSendService = httpResponseSendService();
        this.httpRequestFactory = httpRequestFactory();
        this.filter = filter();
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());
        try (InputStream in = connection.getInputStream();
             OutputStream out = connection.getOutputStream()) {
            BufferedReader inBufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            HttpRequest httpRequest = httpRequestFactory.create(inBufferedReader);
            HttpResponseDto httpResponseDto = new HttpResponseDto();

            logger.debug(httpRequest.toString());

            filter.doFilter(httpRequest, httpResponseDto);

            HttpResponse httpResponse = httpResponseFactory.create(httpResponseDto);
            httpResponseSendService.sendHttpResponse(out, httpResponse);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
