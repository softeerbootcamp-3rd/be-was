package controller;

import service.UserService;
import utils.FileUtil;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import webserver.http.response.HttpResponseBuilder;
import webserver.http.response.HttpStatus;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class UserController {
    private final UserService userService = new UserService();

    public HttpResponse signUp(HttpRequest request) {
        Map<String, String> queryParams = request.getQueryParams();
        HttpResponseBuilder responseBuilder = new HttpResponseBuilder();
        if(userService.signUp(queryParams)){
            // 회원가입 성공
            return responseBuilder.createRedirectResponse(HttpStatus.SEE_OTHER, "/user/login.html");
        }
        // 회원가입 실패
        return responseBuilder.createErrorResponse(HttpStatus.BAD_REQUEST, "SignUp Failed".getBytes(StandardCharsets.UTF_8));
    }

}
