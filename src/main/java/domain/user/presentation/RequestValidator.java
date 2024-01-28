package domain.user.presentation;

import common.http.response.HttpStatusCode;
import common.utils.ResponseUtils;
import domain.user.command.application.UserCreateRequest;
import java.util.HashMap;
import webserver.container.ResponseThreadLocal;

public class RequestValidator {
    public static boolean userCreateRequestValidate(UserCreateRequest userCreateRequest) {
        if (userCreateRequest.getUserId() == null ||
            userCreateRequest.getPassword() == null ||
            userCreateRequest.getName() == null ||
            userCreateRequest.getEmail() == null) {
            ResponseThreadLocal.onFailure(HttpStatusCode.BAD_REQUEST, new HashMap<>(), "userId is null".getBytes());
            return true;
        }
        return false;
    }

    public static boolean userLoginRequestValidate(UserLoginRequest userLoginRequest) {
        if (userLoginRequest.getUserId() == null || userLoginRequest.getPassword() == null) {
            ResponseThreadLocal.onFailure(HttpStatusCode.FOUND, ResponseUtils.makeLoginFailedHeader(), new byte[0]);
            return true;
        }
        return false;
    }
}
