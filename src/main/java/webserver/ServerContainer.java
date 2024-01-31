package webserver;

import common.http.request.Body;
import common.http.request.Header;
import common.http.request.HttpMethod;
import common.http.request.HttpRequest;
import common.http.request.StartLine;
import common.http.response.HttpResponse;
import common.http.response.HttpStatusCode;
import common.logger.CustomLogger;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import webserver.container.CustomThreadLocal;
import webserver.container.Dispatcher;

public class ServerContainer implements Runnable {

    private final Socket connection;

    public ServerContainer(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest httpRequest = httpRequestParser(in);
            HttpResponse response = createInitResponse();

            CustomThreadLocal.setHttpRequest(httpRequest);
            CustomThreadLocal.setHttpResponse(response);
//            CustomLogger.printIPAndPort(connection);
            CustomLogger.printRequest(httpRequest);

            Dispatcher dispatcher = Dispatcher.getInstance();
            dispatcher.process(httpRequest);

            DataOutputStream dos = new DataOutputStream(out);
            HttpResponse httpResponse = CustomThreadLocal.getHttpResponse();

            byte[] convertResponseToByteArray = httpResponse.convertResponseToByteArray();
            dos.write(convertResponseToByteArray, 0, convertResponseToByteArray.length);
            dos.flush();
        } catch (Exception e) {
            CustomLogger.printError(e);
        } finally {
            CustomThreadLocal.clearHttpRequest();
            CustomThreadLocal.clearHttpResponse();
        }
    }

    private HttpRequest httpRequestParser(InputStream in) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(
            new InputStreamReader(in, "UTF-8"));

        String line = bufferedReader.readLine();
        StartLine startLine = parsingStringLine(line);

        Map<String, String> headers = new HashMap<>();
        line = bufferedReader.readLine();
        while (!line.isEmpty()) {
            headers.put(line.split(": ")[0], line.split(": ")[1]);
            line = bufferedReader.readLine();
        }

        Header header = new Header(headers);

        StringBuilder requestBodyBuilder = new StringBuilder();
        if (startLine.getHttpMethod() == HttpMethod.POST) {
            int contentLength = Integer.parseInt(header.getSpecificHeader("Content-Length"));
            char[] buffer = new char[1024];
            int bytesRead;
            while (contentLength > 0 && (bytesRead = bufferedReader.read(buffer, 0, Math.min(buffer.length, contentLength))) != -1) {
                requestBodyBuilder.append(buffer, 0, bytesRead);
                contentLength -= bytesRead;
            }
        }

        Body body = new Body(parseRequestBody(requestBodyBuilder.toString()));
        return new HttpRequest(startLine, header, body);
    }

    private Map<String, String> parseRequestBody(String requestBody)
        throws UnsupportedEncodingException {
        Map<String, String> parsedBody = new HashMap<>();
        if (!requestBody.isEmpty()) {
            String[] pairs = requestBody.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    String key = URLDecoder.decode(keyValue[0], "UTF-8");
                    String value = URLDecoder.decode(keyValue[1], "UTF-8");
                    parsedBody.put(key, value);
                }
            }
        }
        return parsedBody;
    }


    private StartLine parsingStringLine(String startLine) {
        String[] startLines = startLine.split(" ");
        return new StartLine(startLines[0], startLines[1], startLines[2]);
    }

    private HttpResponse createInitResponse() {
        return HttpResponse.responseBuilder(
            HttpStatusCode.OK,
            new HashMap<>(),
            null
        );
    }
}
