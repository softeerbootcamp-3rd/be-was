package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import config.AppConfig;
import model.http.request.HttpRequest;
import model.http.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import factory.HttpRequestFactory;
import service.HttpResponseService;
import factory.HttpResponseFactory;
import service.WebServerFileService;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
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
            HttpResponseFactory httpResponseFactory = appConfig.httpResponseFactory();;
            HttpResponseService httpResponseService = appConfig.httpResponseService();
            HttpRequestFactory httpRequestFactory = appConfig.httpRequestFactory();
            WebServerFileService webServerFileService = appConfig.webServerFileService();
            HttpRequest httpRequest = httpRequestFactory.create(inBufferedReader);

            logger.debug(httpRequest.toString());
            byte[] body = webServerFileService.getFile(httpRequest);
            HttpResponse httpResponse = httpResponseFactory.create(httpRequest, body);
            httpResponseService.sendHttpResponse(out, httpResponse);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}