package controller;

import common.validate.InputValidate;
import dto.HttpResponse;
import dto.LoginRequest;
import model.User;

import static common.binder.Binder.bindQueryStringToObject;
import static session.SessionManager.*;
import static webserver.WebServerConfig.INDEX_FILE_PATH;
import static webserver.WebServerConfig.userService;

public class UserController {

    public HttpResponse create(String userInfo) throws Exception {
        InputValidate.validateUserInfo(userInfo);
        User user = bindQueryStringToObject(userInfo, User.class);
        userService.create(user);
        return new HttpResponse().makeRedirect(INDEX_FILE_PATH);
    }

    public HttpResponse login(String loginInfo) throws Exception {
        LoginRequest loginRequest = bindQueryStringToObject(loginInfo, LoginRequest.class);
        User user = userService.login(loginRequest);

        HttpResponse response = new HttpResponse().makeRedirect(INDEX_FILE_PATH);
        response.addHeader("Set-Cookie", "SID=" + createSessionId(user) + "; Path=/");
        return response;
    }
}
