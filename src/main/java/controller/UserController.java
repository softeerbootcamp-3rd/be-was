package controller;

import dto.HttpResponse;
import dto.LoginRequest;
import model.User;

import static common.Binder.bindQueryStringToObject;
import static common.InputValidator.*;
import static common.WebServerConfig.*;
import static session.SessionManager.*;

public class UserController {

    public static HttpResponse create(String userInfo) throws Exception {
        validateForm(userInfo);
        User user = bindQueryStringToObject(userInfo, User.class);
        userService.create(user);
        return new HttpResponse().makeRedirect(INDEX_FILE_PATH);
    }

    public static HttpResponse login(String loginInfo) throws Exception {
        if (validateForm(loginInfo)) {
            LoginRequest loginRequest = bindQueryStringToObject(loginInfo, LoginRequest.class);
            User user = userService.login(loginRequest);

            if (user != null) {
                HttpResponse response = new HttpResponse().makeRedirect(INDEX_FILE_PATH);
                response.addHeader("Set-Cookie", "SID=" + createSessionId(user) + "; Path=/");
                return response;
            }
        }
        return new HttpResponse().makeRedirect(LOGIN_FAIL_FILE_PATH);
    }
}
