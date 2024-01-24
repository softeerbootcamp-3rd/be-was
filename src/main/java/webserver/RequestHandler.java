package webserver;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.net.Socket;
import java.util.Map;

import common.exception.LoginFailException;
import dto.HttpRequest;
import common.exception.DuplicateUserIdException;
import common.exception.EmptyFormException;
import dto.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static common.response.Status.*;
import static common.util.Util.getSessionId;
import static webserver.WebServerConfig.*;

public class RequestHandler implements Runnable {
    public static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            HttpRequest request = new HttpRequest(br);
            HttpResponse response = new HttpResponse(out, request);

            if (request.getMethod().equals("GET")) {
                response.setStatus(OK);
                response.setPath(request.getPath());
            }
            if (request.getMethod().equals("POST")) {
                int contentLength = getContentLength(request);
                String body = getRequestBody(br, contentLength);
                if (request.getPath().equals("/user/create")) {
                    try {
                        userController.create(body);
                        response.setStatus(REDIRECT);
                        response.setPath(INDEX_FILE_PATH);
                    } catch (EmptyFormException e) {
                        logger.debug(e.getMessage());
                        response.setStatus(BAD_REQUEST);
                        response.setPath(USER_CREATE_FORM_FAIL_FILE_PATH);
                    }
                    catch (DuplicateUserIdException e) {
                        logger.debug(e.getMessage());
                        response.setStatus(CONFLICT);
                        response.setPath(USER_CREATE_DUPLICATE_USERID_FAIL_FILE_PATH);
                    }
                }
                if (request.getPath().equals("/user/login")) {
                    try {
                        userController.login(body);
                        response.setStatus(REDIRECT);
                        response.setPath(INDEX_FILE_PATH);
                        response.setHeader("Set-Cookie", "SID=" + getSessionId());
                    } catch (LoginFailException e) {
                        logger.debug(e.getMessage());
                        response.setStatus(REDIRECT);
                        response.setPath(LOGIN_FAIL_FILE_PATH);
                    }
                }
            }
            response.send();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private int getContentLength(HttpRequest request) {
        Map<String, String> headers = request.getHeaders();
        String contentLength = headers.get("Content-Length");
        return Integer.parseInt(contentLength);
    }

    private String getRequestBody(BufferedReader br, int contentLength) throws IOException {
        char[] buffer = new char[contentLength];
        br.read(buffer, 0, contentLength);
        return new String(buffer);
    }
}
