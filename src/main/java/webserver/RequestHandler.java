package webserver;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashSet;

import controller.MainController;
import model.Request;
import model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final HashSet<String> printedKey =  // logger.debug로 출력할 헤더값들
            new HashSet<>(Arrays.asList("Accept", "Host", "User-Agent", "Cookie"));
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
            responseProcess(dos, response);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
    private void responseProcess(DataOutputStream dos, Response response) {
        String statusCode = response.getStatusCode();
        if(statusCode.equals("200")) {
            byte[] body = response.getBody();
            String mimeType = response.getMimeType();
            response200Header(dos, mimeType, body.length);
            responseBody(dos, body);
        }
        else if(statusCode.equals("302")) {
            response302Header(dos, response.getRedirectUrl());
        }
        else if(statusCode.equals("404")){
            byte[] body = response.getBody();
            String mimeType = response.getMimeType();
            response404Header(dos, mimeType, body.length);
            responseBody(dos, body);
        }
    }

    private void response200Header(DataOutputStream dos,
                                   String mimeType,
                                   int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + mimeType + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String redirectUrl) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + redirectUrl);
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
    private void response404Header(DataOutputStream dos,
                                   String mimeType,
                                   int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 404 Not Found \r\n");
            dos.writeBytes("Content-Type: " + mimeType + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
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
    private Request handleRequest(BufferedReader br) throws IOException {
        Request request = new Request();
        request = handleRequestStartLine(br, request); // startLine 처리
        request = handleRequestHeader(br, request); // header 처리
        request = handleRequestBody(br, request); // body 처리
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
            request.putHeader(key, value);
            if(printedKey.contains(key))
                logger.debug(line);
        }
        return request;
    }
    private Request handleRequestBody(BufferedReader br, Request request) throws IOException {
        String contentLength = request.getHeader().getOrDefault("Content-Length", "0");
        if(contentLength.equals("0")) return request;
        //TODO
        return request;
    }
}
