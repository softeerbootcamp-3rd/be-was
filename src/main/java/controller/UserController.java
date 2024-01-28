package controller;

import annotation.PostMapping;
import webserver.HttpRequest;
import webserver.HttpResponse;
import service.UserService;

import static constant.StaticFile.LOGIN_FAILED_PATH;
import static constant.StaticFile.MAIN_PAGE_PATH;


public class UserController {

    @PostMapping(path = "/user/create")
    public static HttpResponse create(HttpRequest httpRequest) {
        UserService.signUp(httpRequest.getBody());

        return new HttpResponse().makeRedirect(MAIN_PAGE_PATH);
    }

    @PostMapping(path = "/user/login")
    public static HttpResponse login(HttpRequest httpRequest) {
        HttpResponse httpResponse = new HttpResponse();

        String sessionId;
        String userId = httpRequest.getBody().get("userId");
        String password = httpRequest.getBody().get("password");

        if ((sessionId = UserService.login(userId, password)) != null ) {
            httpResponse.addHeader("Set-Cookie", ("sessionId=" + sessionId + "; Path=/"));
            httpResponse.makeRedirect(MAIN_PAGE_PATH);
        } else {
            httpResponse.makeRedirect(LOGIN_FAILED_PATH);
        }
        // UserService.login 하고 생성한 유저의 sessionId 값을 응답 헤더에 추가해 응답 리턴

        return httpResponse;
    }
}
