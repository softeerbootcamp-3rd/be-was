package webserver;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.JdbcUtil;
import util.SessionManager;
import util.StringParser;
import webserver.http.*;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final List<String> AUTHORIZED_URI = List.of(new String[]{"/user/list.html", "/write.html"});

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

            // 세션 만료 확인
            String SID = StringParser.getCookieValue(httpRequest.getCookie(), "SID");
            // 1. 세션이 유효하지 않으면
            // 세션 DB에서 세션 삭제 후
            // 로그인 페이지로 redirect
            if ( SID != null && !SessionManager.isValidateSession(SID) ) {
                SessionManager.deleteSession(SID);
                Map<String, List<String>> headerMap = new HashMap<>();
                // 브라우저에서도 쿠키 삭제
                List<String> cookies = new ArrayList<>();
                cookies.add("SID=" + SID);
                cookies.add("Max-Age=" + 0);
                cookies.add("Path=" + "/");
                headerMap.put(HttpHeader.SET_COOKIE, cookies);
                headerMap.put(HttpHeader.LOCATION, Collections.singletonList("/user/login.html"));
                ResponseEntity response = new ResponseEntity<>(HttpStatus.FOUND, headerMap);
                HttpResponse.send(dos, response);
                return;
            }

            // 2. 권한이 없는 페이지에 접근을 하면
            // 로그인 페이지로 redirect
            if (AUTHORIZED_URI.contains(httpRequest.getPath())) {
                if (SID == null || !SessionManager.isSessionExist(SID)) {
                    Map<String, List<String>> headerMap = new HashMap<>();
                    headerMap.put(HttpHeader.LOCATION, Collections.singletonList("/user/login.html"));
                    ResponseEntity response = new ResponseEntity<>(HttpStatus.FOUND, headerMap);
                    HttpResponse.send(dos, response);
                    return;
                }
            }


            ResponseEntity response = RequestMappingHandler.handleRequest(httpRequest);
            if (SID != null && SessionManager.isSessionExist(SID)) {
                SessionManager.updateLastAccessedTime(SID);
            }

            HttpResponse.send(dos, response);

        } catch (IndexOutOfBoundsException e) {
            HttpResponse.send(dos, new ResponseEntity(HttpStatus.BAD_REQUEST));
        } catch (Exception e) {
            HttpResponse.send(dos, new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

}