package webserver;

import handler.RequestHandler;
import handler.ResponseHandler;
import http.request.HttpRequest;
import http.request.HttpRequestBody;
import http.request.HttpRequestHeader;
import http.request.HttpRequestStartLine;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import logger.CustomLogger;

public class HttpProcessor implements Runnable {

    private final Socket connection;

    public HttpProcessor(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // HTTP Request Header 읽기
            HttpRequest httpRequest = httpRequestParser(in);

            // HTTP Request Header 및 IP,Port 출력하기
            CustomLogger.printIPAndPort(connection);
            CustomLogger.printRequest(httpRequest);

            // HTTP Request Handler
            RequestHandler requestHandler = RequestHandler.getInstance();
            byte[] body = requestHandler.dispatcher(httpRequest);

            // HTTP Response Handler
            ResponseHandler responseHandler = ResponseHandler.getInstance();
            responseHandler.process(out, body);
        } catch (Exception e) {
            CustomLogger.printError(e);
        }
    }

    private HttpRequest httpRequestParser(InputStream in) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

        String line = bufferedReader.readLine();
        HttpRequestStartLine httpRequestStartLine = parsingStringLine(line);

        LinkedHashMap<String, String> headers = new LinkedHashMap<>();
        line = bufferedReader.readLine();
        while(!line.isEmpty()) {
            headers.put(line.split(": ")[0], line.split(": ")[1]);
            line = bufferedReader.readLine();
        }
        HttpRequestHeader httpRequestHeader = new HttpRequestHeader(headers);

        // TODO: HTTP Request Body Parser 구현
        HttpRequestBody httpRequestBody = new HttpRequestBody(new HashMap<>());
        return new HttpRequest(httpRequestStartLine, httpRequestHeader, httpRequestBody);
    }

    private HttpRequestStartLine parsingStringLine(String startLine) {
        String[] startLines = startLine.split(" ");
        return new HttpRequestStartLine(startLines[0], startLines[1], startLines[2]);
    }
}
