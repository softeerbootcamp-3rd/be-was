package controller;

import service.UserService;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import webserver.http.response.HttpResponseBuilder;
import webserver.http.response.enums.HttpStatus;

import java.util.Map;

public class UserController {
    private final UserService userService = new UserService();

    public HttpResponse signUp(HttpRequest request) {
        Map<String, String> bodyParams = request.getBody();
        HttpResponseBuilder responseBuilder = new HttpResponseBuilder();
        if(userService.signUp(bodyParams)){
            // 회원가입 성공
            return responseBuilder.createRedirectResponse(HttpStatus.FOUND, "/index.html");
        }
        // 회원가입 실패
        return responseBuilder.createErrorResponse(HttpStatus.BAD_REQUEST, "SignUp Failed".getBytes());
    }

    public HttpResponse login(HttpRequest request){
        Map<String, String> bodyParams = request.getBody();
        HttpResponseBuilder responseBuilder = new HttpResponseBuilder();
        if(userService.login(bodyParams)){
            // 로그인 성공
            return responseBuilder.createRedirectResponse(HttpStatus.FOUND, "/index.html");
        }
        // 로그인 실패
        return responseBuilder.createRedirectResponse(HttpStatus.FOUND, "/user/login_failed.html");
    }

}
