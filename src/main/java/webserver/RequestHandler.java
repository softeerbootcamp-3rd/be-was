package webserver;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.net.Socket;

import dto.RequestLineDto;
import common.exception.DuplicateUserIdException;
import common.exception.EmptyFormException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static common.config.WebServerConfig.userController;
import static common.response.Status.*;
import static common.view.OutputView.*;
import static webserver.RequestParser.*;
import static webserver.Response.*;

public class RequestHandler implements Runnable {
    public static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final String INDEX_FILE_PATH = "/index.html";
    private static final String USER_CREATE_FORM_FAIL_FILE_PATH = "/user/form_fail.html";
    private static final String USER_CREATE_DUPLICATE_USERID_FAIL_FILE_PATH = "/user/form_userId_duplicate_fail.html";

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String line = br.readLine();
            RequestLineDto requestLineDto = parseRequestLine(line);
            printRequest(requestLineDto);

            int contentLength = 0;
            while (!line.equals("")) {
                line = br.readLine();
                if (line.contains("Content-Length")) {
                    contentLength = getContentLength(line);
                }
            }

            String contentType = getContentType(requestLineDto.getPath());

            if (contentLength == 0) {
                createResponse(out, OK, contentType, requestLineDto.getPath());
            }
            if (requestLineDto.getMethod().equals("POST") && requestLineDto.getPath().equals("/user/create")) {
                String body = getRequestBody(br, contentLength);
                try {
                    userController.create(body);
                    createResponse(out, REDIRECT, contentType, INDEX_FILE_PATH);
                } catch (EmptyFormException e) {
                    logger.debug(e.getMessage());
                    createResponse(out, BAD_REQUEST, contentType, USER_CREATE_FORM_FAIL_FILE_PATH);
                }
                catch (DuplicateUserIdException e) {
                    logger.debug(e.getMessage());
                    createResponse(out, CONFLICT, contentType, USER_CREATE_DUPLICATE_USERID_FAIL_FILE_PATH);
                }
            }
            if (requestLineDto.getMethod().equals("POST") && requestLineDto.getPath().equals("/user/login")) {
                String body = getRequestBody(br, contentLength);
                userController.login(body);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private int getContentLength(String line) {
        String[] tokens = parseRequestHeader(line);
        String contentLength = tokens[1];
        return Integer.parseInt(contentLength);
    }

    private String getRequestBody(BufferedReader br, int contentLength) throws IOException {
        char[] buffer = new char[contentLength];
        br.read(buffer, 0, contentLength);
        return new String(buffer);
    }
}
