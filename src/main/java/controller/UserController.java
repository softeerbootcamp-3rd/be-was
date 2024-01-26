package controller;

import annotation.Controller;
import annotation.RequestBody;
import annotation.RequestMapping;
import constant.HttpHeader;
import constant.HttpStatus;
import db.UserDatabase;
import dto.LoginDto;
import dto.UserCreateDto;
import model.User;
import util.RequestParser;
import util.SessionManager;
import webserver.HttpRequest;
import webserver.HttpResponse;

import java.util.Map;

@Controller
public class UserController {

    @RequestMapping(method = "POST", path = "/user/create")
    public static HttpResponse createUser(@RequestBody UserCreateDto user) {
        User existUser = UserDatabase.findById(user.getUserId());

        if (existUser != null)
            return HttpResponse.builder()
                    .status(HttpStatus.CONFLICT)
                    .body("The requested username is already in use.")
                    .build();

        UserDatabase.add(new User(user.getUserId(), user.getPassword(), user.getName(), user.getEmail()));
        return HttpResponse.builder()
                .status(HttpStatus.FOUND)
                .addHeader(HttpHeader.LOCATION, "/index.html")
                .build();
    }

    @RequestMapping(method = "POST", path = "/user/login")
    public static HttpResponse login(@RequestBody LoginDto loginInfo) {
        User existUser = UserDatabase.findById(loginInfo.getUserId());
        if (existUser == null || !existUser.getPassword().equals(loginInfo.getPassword()))
            return HttpResponse.builder()
                    .status(HttpStatus.FOUND)
                    .addHeader(HttpHeader.LOCATION, "/user/login_failed.html")
                    .build();

        String sessionId = SessionManager.generateSessionId();
        SessionManager.addSession(sessionId, existUser);

        return HttpResponse.builder()
                .status(HttpStatus.FOUND)
                .addHeader(HttpHeader.SET_COOKIE, "SID=" + sessionId + "; Path=/;")
                .addHeader(HttpHeader.LOCATION, "/index.html")
                .build();
    }

    @RequestMapping(method = "GET", path = "/user/logout")
    public static HttpResponse logout(HttpRequest request) {
        Map<String, String> cookies = RequestParser.parseCookie(request.getHeader().get(HttpHeader.COOKIE));
        String sessionId = cookies.get("SID");
        SessionManager.removeSession(sessionId);

        return HttpResponse.builder()
                .status(HttpStatus.FOUND)
                .addHeader(HttpHeader.LOCATION, "/index.html")
                .build();
    }
}
