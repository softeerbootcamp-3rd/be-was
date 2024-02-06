package controller;

import annotation.GetMapping;
import annotation.PostMapping;
import annotation.RequestParam;
import model.User;
import request.HttpRequest;
import response.HttpResponse;
import response.HttpResponseBuilder;
import response.HttpResponseStatus;
import service.UserJoinService;
import service.UserLoginService;
import session.SessionManager;
import view.ViewResolver;

import java.util.HashMap;
import java.util.Map;

public class UserController {
    Map<String, String> responseHeaders = new HashMap<>();
    final String LOCATION = "Location";
    final String CONTENT_TYPE = "Content-Type";
    final String CONTENT_LENGTH = "Content-Length";
    final String SET_COOKIE = "set-cookie";
    SessionManager sessionManager = new SessionManager();

    @PostMapping(url = "/user/create")
    public String userJoin(@RequestParam(name = "userId") String userId,
                                 @RequestParam(name = "password") String password,
                                 @RequestParam(name = "name") String name,
                                 @RequestParam(name = "email") String email) {
        UserJoinService userJoinService = new UserJoinService();

        if (userJoinService.createUser(userId, password, name, email)) { // 회원 가입 성공 시 홈으로
            return "/index.html";
        } else { // 회원 가입 실패 시 다시 회원 가입 창으로
            return "/user/form.html";
        }
    }

    @PostMapping(url = "/user/login")
    public HttpResponse userLogin(@RequestParam(name = "userId") String userId,
                                  @RequestParam(name = "password") String password) {
        UserLoginService userLoginService = new UserLoginService();

        User loginUser = userLoginService.login(userId, password);
        if (loginUser == null) { // 로그인 실패 시 로그인 실패 창으로
            responseHeaders.put(LOCATION, "/user/login_failed.html");
        } else { // 로그인 성공 시 홈으로
            String sessionId = sessionManager.createSession(loginUser);
            responseHeaders.put(LOCATION, "/index.html");
            responseHeaders.put(SET_COOKIE, "sid="+sessionId+"; Max-Age=300; Path=/");
        }
        return new HttpResponseBuilder().status(HttpResponseStatus.FOUND)
                .headers(responseHeaders).build();
    }

    @PostMapping(url = "/user/logout")
    public String userLogout(HttpRequest request) {
        sessionManager.deleteSession(request);
        return "/index.html";
    }

    @GetMapping(url = "/user/list.html")
    public HttpResponse getList(HttpRequest request) {
        Map<String, Object> model = new HashMap<>();

        if(!request.isAuth()) {
            HttpResponse response = new HttpResponse();
            responseHeaders.put("location", "/user/login.html");
            response.setResponse(HttpResponseStatus.FOUND, null, responseHeaders);
            return response;
        } else {
            User loginUser = sessionManager.getUserBySessionId(request);
            model.put("name", loginUser.getName());

            byte[] body = ViewResolver.resolve(request.getUrl(), request.isAuth(), model);
            responseHeaders.put(CONTENT_TYPE, "text/html; charset=utf-8");
            responseHeaders.put(CONTENT_LENGTH, String.valueOf(body.length));
            return new HttpResponseBuilder().status(HttpResponseStatus.OK)
                    .headers(responseHeaders)
                    .body(body)
                    .build();
        }

    }
}
