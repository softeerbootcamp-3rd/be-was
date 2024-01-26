package webserver;

import common.exception.DuplicateUserIdException;
import common.exception.EmptyFormException;
import common.exception.LoginFailException;
import dto.HttpResponse;

import java.io.FileNotFoundException;
import java.io.IOException;

import static webserver.RequestHandler.logger;
import static webserver.Status.*;
import static webserver.WebServerConfig.*;

public class ExceptionHandler {

    public static void process(Exception e, HttpResponse response) throws IOException {
        logger.debug(e.getMessage());
        e.printStackTrace();
        if (e.getClass() == EmptyFormException.class) {
            response.makeBody(BAD_REQUEST, USER_CREATE_FORM_FAIL_FILE_PATH);
        }
        if (e.getClass() == DuplicateUserIdException.class) {
            response.makeBody(CONFLICT, USER_CREATE_DUPLICATE_USERID_FAIL_FILE_PATH);
        }
        if (e.getClass() == LoginFailException.class) {
            response.makeBody(REDIRECT, LOGIN_FAIL_FILE_PATH);
        }
        if (e.getClass() == FileNotFoundException.class) {
            response.makeBody(NOT_FOUND, NOT_FOUND_FILE_PATH);
        }
    }
}
