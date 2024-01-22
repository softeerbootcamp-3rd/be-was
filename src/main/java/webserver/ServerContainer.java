package webserver;

import common.http.request.HttpMethod;
import common.http.request.HttpRequest;
import common.http.request.HttpRequestBody;
import common.http.request.HttpRequestHeader;
import common.http.request.HttpRequestStartLine;
import common.http.response.HttpResponse;
import common.http.response.HttpStatusCode;
import common.logger.CustomLogger;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import webserver.container.Dispatcher;
import webserver.container.ResponseThreadLocal;

public class ServerContainer implements Runnable {

    private final Socket connection;

    public ServerContainer(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest httpRequest = httpRequestParser(in);
            HttpResponse response = createInitResponse();

            ResponseThreadLocal.setHttpResponse(response);
//            CustomLogger.printIPAndPort(connection);
            CustomLogger.printRequest(httpRequest);

            Dispatcher dispatcher = Dispatcher.getInstance();
            dispatcher.process(httpRequest);

            DataOutputStream dos = new DataOutputStream(out);
            HttpResponse httpResponse = ResponseThreadLocal.getHttpResponse();

            dos.write(httpResponse.convertResponseToByteArray(), 0, httpResponse.convertResponseToByteArray().length);
            ResponseThreadLocal.clearHttpResponse();
            dos.flush();
        } catch (Exception e) {
            CustomLogger.printError(e);
        }
    }

    private HttpRequest httpRequestParser(InputStream in) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(
            new InputStreamReader(in, StandardCharsets.UTF_8));

        String line = bufferedReader.readLine();
        HttpRequestStartLine httpRequestStartLine = parsingStringLine(line);

        HashMap<String, String> headers = new HashMap<>();
        line = bufferedReader.readLine();
        while (!line.isEmpty()) {
            headers.put(line.split(": ")[0], line.split(": ")[1]);
            line = bufferedReader.readLine();
        }

        HttpRequestHeader httpRequestHeader = new HttpRequestHeader(headers);

        StringBuilder requestBodyBuilder = new StringBuilder();
        if (httpRequestStartLine.getHttpMethod() == HttpMethod.POST) {
            int contentLength = Integer.parseInt(httpRequestHeader.getSpecificHeader("Content-Length"));
            char[] buffer = new char[1024];
            int bytesRead;
            while (contentLength > 0 && (bytesRead = bufferedReader.read(buffer, 0, Math.min(buffer.length, contentLength))) != -1) {
                requestBodyBuilder.append(buffer, 0, bytesRead);
                contentLength -= bytesRead;
            }
        }

        HttpRequestBody httpRequestBody = new HttpRequestBody(parseRequestBody(requestBodyBuilder.toString()));
        return new HttpRequest(httpRequestStartLine, httpRequestHeader, httpRequestBody);
    }

    private Map<String, String> parseRequestBody(String requestBody) {
        Map<String, String> parsedBody = new HashMap<>();
        if (!requestBody.isEmpty()) {
            String[] pairs = requestBody.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8);
                    String value = URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8);
                    parsedBody.put(key, value);
                }
            }
        }
        return parsedBody;
    }


    private HttpRequestStartLine parsingStringLine(String startLine) {
        String[] startLines = startLine.split(" ");
        return new HttpRequestStartLine(startLines[0], startLines[1], startLines[2]);
    }

    private HttpResponse createInitResponse() {
        return HttpResponse.responseBuilder(
            HttpStatusCode.OK,
            new HashMap<>(),
            null
        );
    }
}
