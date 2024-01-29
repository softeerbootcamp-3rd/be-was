package controller;

import annotation.GetMapping;
import annotation.PostMapping;
import session.Session;
import session.SessionManager;
import webserver.ThreadLocalManager;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;
import service.UserService;

import static constant.FileConstant.LOGIN_FAILED_PAGE_PATH;
import static constant.FileConstant.MAIN_PAGE_PATH;


public class UserController {

    @PostMapping("/user/create")
    public static HttpResponse create(HttpRequest httpRequest) {
        UserService.signUp(httpRequest.getBody());

        return new HttpResponse().makeRedirect(MAIN_PAGE_PATH);
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
            httpResponse.makeRedirect(MAIN_PAGE_PATH);
        } else {
            httpResponse.makeRedirect(LOGIN_FAILED_PAGE_PATH);
        }

        return httpResponse;
    }

    @GetMapping("/user/logout")
    public static HttpResponse logout(HttpRequest httpRequest) {
        HttpResponse httpResponse = new HttpResponse();

        String sessionId = ThreadLocalManager.getSessionId();
        SessionManager.removeSession(sessionId);

        httpResponse.makeRedirect(MAIN_PAGE_PATH);
        return httpResponse;
    }
}
