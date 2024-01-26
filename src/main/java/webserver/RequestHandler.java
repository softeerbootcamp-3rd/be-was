package webserver;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import controller.MainController;
import model.Request;
import model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Util;

public class RequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final HashSet<String> printedKey =  // logger.debug로 출력할 헤더값들
            new HashSet<>(Arrays.asList("Accept", "Cookie")); // else "Host", "User-Agent"
    private BufferedReader bufferedReader;
    private Request request;

    public RequestHandler(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
        this.request = new Request();
    }
    public Request handleRequest() throws IOException {
        handleRequestStartLine(); // startLine 처리
        handleRequestHeader(); // header 처리
        handleRequestBody(); // body 처리
        return this.request;
    }
    private void handleRequestStartLine() throws IOException {
        String line = this.bufferedReader.readLine();
        request.parseStartLine(line);
        logger.debug(this.request.toString());
    }
    private void handleRequestHeader() throws IOException {
        String line;
        while((line = this.bufferedReader.readLine()) != null) {
            if(line.isEmpty()) break;
            String[] keyValue = line.split(": ");
            String key = keyValue[0], value = keyValue[1];
            request.putHeader(key.toLowerCase(), value);
            if(printedKey.contains(key))
                logger.debug(line);
        }
        request.parseCookie();
    }
    private void handleRequestBody() throws IOException {
        int contentLength = Integer.parseInt(request.getHeader().getOrDefault("content-length", "0"));
        if(contentLength == 0) return;
        String contentType = request.getHeader().get("content-type");
        char[] body = new char[contentLength];
        this.bufferedReader.read(body);

        if("application/x-www-form-urlencoded".equals(contentType)) {
            HashMap<String, String> hashMap = Util.parseQueryString(new String(body));
            request.setBody(hashMap);
        }
        else if("application/json".equals(contentType)) {
            HashMap<String, String> hashMap = Util.parseStringJson(new String(body));
            request.setBody(hashMap);
        }
    }
}