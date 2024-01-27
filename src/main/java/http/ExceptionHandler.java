package http;

import common.exception.DuplicateUserIdException;
import common.exception.EmptyFormException;
import common.exception.LoginFailException;
import dto.HttpResponse;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import static webserver.RequestHandler.logger;
import static http.constants.Status.*;
import static common.WebServerConfig.*;

public class ExceptionHandler {

    public static void process(Exception e, HttpResponse response) throws IOException {
        logger.debug(e.getMessage());
        Class<? extends Throwable> exceptionClass = e.getClass();

        // method.invoke 내부에서 발생한 에러인 경우, 그 내부에 있는 원인 예외의 클래스를 얻어서 예외를 처리한다.
        if (e.getClass() == InvocationTargetException.class) {
            exceptionClass = ((InvocationTargetException) e).getTargetException().getClass();
        }

        // 예외클래스별 응답 생성
        if (exceptionClass == EmptyFormException.class) {
            response.makeBody(BAD_REQUEST, USER_CREATE_FORM_FAIL_FILE_PATH);
        }
        if (exceptionClass == DuplicateUserIdException.class) {
            response.makeBody(CONFLICT, USER_CREATE_DUPLICATE_USERID_FAIL_FILE_PATH);
        }
        if (exceptionClass == LoginFailException.class) {
            response.makeBody(REDIRECT, LOGIN_FAIL_FILE_PATH);
        }
        if (exceptionClass == FileNotFoundException.class) {
            response.makeBody(NOT_FOUND, NOT_FOUND_FILE_PATH);
        }
    }
}
