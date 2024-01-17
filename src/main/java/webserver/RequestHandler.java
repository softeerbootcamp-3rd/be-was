package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            HttpRequestParser httpRequestParser = new HttpRequestParser();
            HttpRequest httpRequest = httpRequestParser.parse(in);
            requestLogging(httpRequest);

            DataOutputStream dos = new DataOutputStream(out);
            HttpResponseBuilder responseBuilder = new HttpResponseBuilder();
            HttpResponse response;

            byte[] body;
            File file = new File("./src/main/resources/templates" + httpRequest.getPath());
            if (file.exists() && !"/".equals(httpRequest.getPath())) {
                body = Files.readAllBytes(file.toPath());
                response = responseBuilder.createSuccessResponse(body);
            } else {
                body = "Valid page is not found".getBytes(StandardCharsets.UTF_8);
                response = responseBuilder.createErrorResponse(body);
            }

            HttpResponseSender sender = new HttpResponseSender();
            sender.sendResponse(response, dos);

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void requestLogging(HttpRequest httpRequest){
        StringBuilder logMessage = new StringBuilder();

        logMessage.append("\n========== HTTP Request ==========\n");
        logMessage.append("Method: ").append(httpRequest.getMethod()).append("\n");
        logMessage.append("Path: ").append(httpRequest.getPath()).append("\n");
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
