package controller;

import http.response.HttpResponse;
import http.request.HttpRequest;
import service.UserService;
import utils.Parser;

import java.util.Map;

/**
 * 스프링의 controller 역할
 * controller - service 연결
 */
public class UserController {
    private static final UserService userService = new UserService();

    public static HttpResponse signup(HttpRequest request) {
        return userService.createUser(request.getBody());
    }
}
