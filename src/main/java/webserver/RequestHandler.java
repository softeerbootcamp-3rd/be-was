package webserver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.File;
import java.net.Socket;
import java.nio.file.*;

import dto.RequestHeaderDto;
import dto.RequestLineDto;
import common.exception.DuplicateUserIdException;
import common.exception.EmptyFormException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import view.OutputView;

import static common.config.WebServerConfig.userController;
import static common.response.Status.*;
import static common.response.Response.createResponse;;

public class RequestHandler implements Runnable {
    public static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final String RESOURCES_PATH = "src/main/resources/";
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
            RequestLineDto requestLineDto = RequestParser.parseRequestLine(br);
            RequestHeaderDto requestHeaderDto = RequestParser.parseRequestHeader(br);
            OutputView.printRequest(requestLineDto, requestHeaderDto);

            String queryString = requestLineDto.getQueryString();
            if (queryString == null) {
                createResponse(out, SUCCESS, requestLineDto.getPath());
            }
            if (requestLineDto.getMethod().equals("GET") && requestLineDto.getPath().equals("/user/create")) {
                try {
                    userController.create(requestLineDto.getQueryString());
                    createResponse(out, REDIRECT, INDEX_FILE_PATH);
                } catch (EmptyFormException e) {
                    logger.debug(e.getMessage());
                    createResponse(out, BAD_REQUEST, USER_CREATE_FORM_FAIL_FILE_PATH);
                }
                catch (DuplicateUserIdException e) {
                    logger.debug(e.getMessage());
                    createResponse(out, CONFLICT, USER_CREATE_DUPLICATE_USERID_FAIL_FILE_PATH);
                }
            }
        } catch (IOException | IllegalAccessException e) {
            logger.error(e.getMessage());
        }
    }
}
