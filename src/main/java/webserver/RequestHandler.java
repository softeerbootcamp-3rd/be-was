package webserver;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import controller.FrontController;
import model.CommonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import request.HttpRequest;
import util.ResponseBuilder;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    static Lock writeLock = lock.writeLock();

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            DataOutputStream dos = new DataOutputStream(out);
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            RequestHeader requestHeader = readRequest(br);
            String body = parseBody(br, Integer.parseInt(requestHeader.getContentLength()));

            HttpRequest httpRequest = HttpRequest.of(requestHeader, body);

            CommonResponse response = FrontController.service(httpRequest);
            ResponseBuilder.sendResponse(dos, response.getBody(), response.getPath(), response.getHttpStatus(), response.getExtension());
        } catch (ClassNotFoundException | IOException e) {
            logger.error(e.getMessage());
        }
    }

    private String parseBody(BufferedReader br, int contentLength) throws IOException {
        char[] chArr = new char[contentLength];
        if (contentLength != 0) {
            br.read(chArr);
        }
        return URLDecoder.decode(new String(chArr), StandardCharsets.UTF_8);
    }

    private RequestHeader readRequest(BufferedReader br) throws IOException, ClassNotFoundException {
        writeLock.lock();
        RequestHeader requestHeader = getRequestUrl(br);
        String line;

        logger.debug("===== request start =====");
        while ((line = br.readLine()) != null) {
            if (line.isBlank()) {
                break;
            }
            parseHeader(line, requestHeader);
        }
        requestHeader.printHeader();
        logger.debug("===== request end =====");
        writeLock.unlock();

        return requestHeader;
    }

    private static RequestHeader getRequestUrl(BufferedReader br) throws IOException {
        String line = URLDecoder.decode(br.readLine(), StandardCharsets.UTF_8);
        String[] firstHeader = line.split(" ");
        return new RequestHeader(firstHeader[0], firstHeader[1], firstHeader[2]);
    }

    private void parseHeader(String line, RequestHeader requestHeader) {
        String[] header = line.split(": ");
        String parseKey = header[0].replace("-", "");
        String key = parseKey.substring(0, 1).toLowerCase() + parseKey.substring(1);
        String value = header[1];
        RequestHeader.setHeader(requestHeader, key, value);
    }
}
