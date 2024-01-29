package controller;

import annotation.Controller;
import annotation.RequestBody;
import annotation.RequestMapping;
import constant.HttpHeader;
import constant.HttpStatus;
import database.UserRepository;
import dto.LoginDto;
import dto.UserCreateDto;
import model.User;
import util.session.SessionManager;
import util.web.RequestParser;
import webserver.HttpRequest;
import webserver.HttpResponse;

import java.util.Map;
import java.util.Objects;

@Controller
public class UserController {

    @RequestMapping(method = "POST", path = "/user/create")
    public static HttpResponse createUser(@RequestBody UserCreateDto user) {
        User existUser = UserRepository.findByUserId(user.getUserId());

        if (existUser != null)
            return HttpResponse.of(HttpStatus.CONFLICT);

        UserRepository.add(new User(user.getUserId(), user.getPassword(), user.getName(), user.getEmail()));
        return HttpResponse.redirect("/index.html");
    }

    @RequestMapping(method = "POST", path = "/user/login")
    public static HttpResponse login(@RequestBody LoginDto loginInfo) {
        User existUser = UserRepository.findByUserId(loginInfo.getUserId());
        if (existUser == null || !Objects.equals(existUser.getPassword(), loginInfo.getPassword()))
            return HttpResponse.redirect("/user/login_failed.html");

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
        return HttpResponse.redirect("/index.html");
    }
}
