package controller;

import annotation.GetMapping;
import annotation.PostMapping;
import session.SessionManager;
import webserver.ThreadLocalManager;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;
import service.UserService;
import webserver.response.ResponseHandler;

import static constant.FileConstant.LOGIN_FAILED_PAGE_PATH;
import static constant.FileConstant.MAIN_PAGE_PATH;


public class UserController {

    @PostMapping("/user/create")
    public static HttpResponse create(HttpRequest httpRequest) {
        HttpResponse httpResponse = new HttpResponse();
        UserService.signUp(httpRequest.getBody());

        ResponseHandler.makeRedirect(httpResponse, MAIN_PAGE_PATH);
        return httpResponse;
    }

    @PostMapping("/user/login")
    public static HttpResponse login(HttpRequest httpRequest) {
        HttpResponse httpResponse = new HttpResponse();

        String sessionId;
        String userId = httpRequest.getBody().get("userId");
        String password = httpRequest.getBody().get("password");

        // todo max-age 설정
        if ((sessionId = UserService.login(userId, password)) != null ) {
            httpResponse.addHeader("Set-Cookie", ("sessionId=" + sessionId + "; Path=/"));
            ResponseHandler.makeRedirect(httpResponse, MAIN_PAGE_PATH);
        } else {
            ResponseHandler.makeRedirect(httpResponse, LOGIN_FAILED_PAGE_PATH);
        }

        return httpResponse;
    }

    @GetMapping("/user/logout")
    public static HttpResponse logout(HttpRequest httpRequest) {
        HttpResponse httpResponse = new HttpResponse();

        String sessionId = ThreadLocalManager.getSessionId();
        SessionManager.removeSession(sessionId);

        ResponseHandler.makeRedirect(httpResponse, MAIN_PAGE_PATH);
        return httpResponse;
    }
}
