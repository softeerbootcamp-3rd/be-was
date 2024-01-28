package controller;

import annotation.PostMapping;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;
import service.UserService;

import static constant.StaticFile.LOGIN_FAILED_PAGE_PATH;
import static constant.StaticFile.MAIN_PAGE_PATH;


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
}
