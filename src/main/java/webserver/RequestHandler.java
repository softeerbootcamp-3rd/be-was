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

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final HashSet<String> printedKey =  // logger.debug로 출력할 헤더값들
            new HashSet<>(Arrays.asList("Accept", "Host", "User-Agent", "Cookie"));
    private static final HashMap<String, String> statusCodeMessage = new HashMap<>() {{
        put("200", "OK"); put("302", "Found"); put("404", "Not Found"); }};
    private Socket connection;
    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("#####  New Client Connect! Connected IP : {}, Port : {}  #####",
                connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            DataOutputStream dos = new DataOutputStream(out);
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            Request request = handleRequest(br);
            Response response = MainController.control(request);
            handleResponse(dos, response);

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
    private Request handleRequest(BufferedReader br) throws IOException {
        Request request = new Request();
        request = handleRequestStartLine(br, request);
        request = handleRequestHeader(br, request);
        request = handleRequestBody(br, request);
        return request;
    }
    private Request handleRequestStartLine(BufferedReader br, Request request) throws IOException {
        String line = br.readLine();
        request.parseStartLine(line);
        logger.debug(request.toString());
        return request;
    }
    private Request handleRequestHeader(BufferedReader br, Request request) throws IOException {
        String line;
        while(true) {
            line = br.readLine();
            if(line == null || line.isEmpty()) break;
            String[] keyAndValue = line.split(": ");
            String key = keyAndValue[0], value = keyAndValue[1];
            request.putHeader(key.toLowerCase(), value);
            if(printedKey.contains(key))
                logger.debug(line);
        }
        request.parseSessionId();
        return request;
    }
    private void handleResponse(DataOutputStream dos, Response response) {
        responseHeader(dos, response);
        if(response.getBody() != null)
            responseBody(dos, response.getBody());
    }
    private void responseHeader(DataOutputStream dos, Response response) {
        String statusCode = response.getStatusCode();
        try {
            dos.writeBytes("HTTP/1.1 " + statusCode + " " + statusCodeMessage.get(statusCode) + " \r\n");
            if(response.getCookie() != null) {
                dos.writeBytes("Set-Cookie: " + "sessionId=" + response.getCookie() + "; Path=/; Max-Age=3600\r\n");
                dos.writeBytes("Location: " + response.getRedirectUrl());
            }
            else if(response.getBody() != null) {
                dos.writeBytes("Content-Type: " + response.getMimeType() + ";charset=utf-8\r\n");
                dos.writeBytes("Content-Length: " + response.getBody().length + "\r\n");
            }
            else if(response.getRedirectUrl() != null){
                dos.writeBytes("Location: " + response.getRedirectUrl());
            }
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
    private Request handleRequestBody(BufferedReader br, Request request) throws IOException {
        int contentLength = Integer.parseInt(request.getHeader().getOrDefault("content-length", "0"));
        if(contentLength == 0) return request;
        String contentType = request.getHeader().get("content-type");
        char[] body = new char[contentLength];
        br.read(body);

        if("application/x-www-form-urlencoded".equals(contentType)) {
            HashMap<String, String> hashMap = Util.parseQueryString(new String(body));
            request.setBody(hashMap);
        }
        else if("application/json".equals(contentType)) {
            HashMap<String, String> hashMap = Util.parseStringJson(new String(body));
            request.setBody(hashMap);
        }

        return request;
    }
}