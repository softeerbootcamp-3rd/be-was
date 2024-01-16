package webserver;

import handler.ResponseHandler;
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
import logger.HttpRequestHeader;

public class HttpProcessor implements Runnable {

    private final Socket connection;

    public HttpProcessor(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // HTTP Request Header 읽기
            HttpRequestHeader httpRequestHeader = httpRequestHeaderParser(in);

            // HTTP Request Header 및 IP,Port 출력하기
            CustomLogger.printIPAndPort(connection);
            CustomLogger.printHeader(httpRequestHeader);

            // HTTP Request Handler
            // TODO: HTTP Request Handler 구현

            // HTTP Response Handler
            ResponseHandler responseHandler = ResponseHandler.initBuilder(true, out, httpRequestHeader.getPath());
            responseHandler.process();
        } catch (IOException e) {
            CustomLogger.printError(e);
        }
    }

    private HttpRequestHeader httpRequestHeaderParser(InputStream in) {
        HashMap<String, String> map = new LinkedHashMap<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            String line = bufferedReader.readLine();
            String[] firstHeader = line.split(" ");
            map.put("method", firstHeader[0]);
            map.put("path", firstHeader[1]);
            map.put("protocol", firstHeader[2]);
            line = bufferedReader.readLine();
            while(!line.isEmpty()) {
                map.put(line.split(": ")[0], line.split(": ")[1]);
                line = bufferedReader.readLine();
            }
        } catch (Exception e) {
            CustomLogger.printError(e);
        }

        return new HttpRequestHeader(map);
    }
}
