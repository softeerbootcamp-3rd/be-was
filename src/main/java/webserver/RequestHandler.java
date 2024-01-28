package webserver;

import java.io.*;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.SessionManager;
import util.StringParser;
import webserver.http.*;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        try {
            InputStream in = connection.getInputStream();
            OutputStream out = connection.getOutputStream();
            DataOutputStream dos = new DataOutputStream(out);

            execute(in, dos);
            out.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void execute(InputStream in, DataOutputStream dos) throws IOException {
        try {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            HttpRequest httpRequest = new HttpRequest(connection, in);
            logger.debug(httpRequest.toString());

            if (httpRequest.getPath().equals("/user/list.html")) {
                String cookie = StringParser.getCookieValue(httpRequest.getCookie(), "SID");
                if (cookie == null || !SessionManager.isSessionExist(cookie)) {
                    Map<String, List<String>> headerMap = new HashMap<>();
                    headerMap.put(HttpHeader.LOCATION, Collections.singletonList("/user/login.html"));
                    ResponseEntity response = new ResponseEntity<>(HttpStatus.FOUND, headerMap);

                    HttpResponse.send(dos, response);
                    return;
                }
            }

            ResponseEntity response = RequestMappingHandler.handleRequest(httpRequest);
            HttpResponse.send(dos, response);
        } catch (IndexOutOfBoundsException e) {
            HttpResponse.send(dos, new ResponseEntity(HttpStatus.BAD_REQUEST));
        } catch (Exception e) {
            HttpResponse.send(dos, new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

}