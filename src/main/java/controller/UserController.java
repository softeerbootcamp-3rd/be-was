package controller;

import annotation.GetMapping;
import annotation.PostMapping;
import annotation.RequestParam;
import db.Database;
import dto.Cookie;
import dto.Response;
import model.User;
import util.SessionManager;
import webserver.HttpRequest;
import webserver.HttpStatus;

import java.util.UUID;


public class UserController {

    @PostMapping(path = "/user/create")
    public static Response signup(@RequestParam(name = "userId") String userId,
                                  @RequestParam(name = "password") String password,
                                  @RequestParam(name = "name") String name,
                                  @RequestParam(name = "email") String email) {

        User user = new User(userId, password, name, email);
        Database.addUser(user);

        return new Response.Builder()
                .httpStatus(HttpStatus.FOUND)
                .body("/index.html")
                .build();
    }

    @PostMapping(path = "/user/login")
    public static Response login(@RequestParam(name = "userId") String userId,
                                 @RequestParam(name = "password") String password) {
        User user = Database.findUserById(userId);

        if (!user.getPassword().equals(password)) {
            return new Response.Builder()
                    .httpStatus(HttpStatus.FOUND)
                    .body("/user/login_failed.html")
                    .build();
        }

        String sid = SessionManager.createSession();
        SessionManager.setAttribute(sid, "user", userId);
        Cookie cookie = new Cookie(sid, SessionManager.getSessionTimeoutSeconds());

        return new Response.Builder()
                .httpStatus(HttpStatus.FOUND)
                .cookie(cookie)
                .body("/index.html")
                .build();
    }

    @GetMapping(path = "/user/logout")
    public static Response logout(HttpRequest httpRequest) {

        String sid = httpRequest.getCookie().split("=")[1];

        // 서버 세션 저장소에서 세션 삭제
        if (SessionManager.checkSessionAvailable(sid))
            SessionManager.invalidateSession(sid);
        // 클라이언트에서도 쿠키 제거
        Cookie cookie = new Cookie(sid, 0);

        return new Response.Builder()
                .httpStatus(HttpStatus.OK)
                .cookie(cookie)
                .build();
    }

}



