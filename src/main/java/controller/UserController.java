package controller;

import annotation.Controller;
import annotation.RequestBody;
import annotation.RequestMapping;
import annotation.RequestParam;
import db.Database;
import model.User;
import util.SessionManager;
import webserver.HttpResponse;
import constant.HttpStatus;

@Controller
public class UserController {

    @RequestMapping(method = "POST", path = "/user/create")
    public static HttpResponse createUser(@RequestBody User user) {
        User existUser = Database.findUserById(user.getUserId());

        if (existUser != null)
            return HttpResponse.builder()
                    .status(HttpStatus.CONFLICT)
                    .body("The requested username is already in use.")
                    .build();

        Database.addUser(user);
        return HttpResponse.builder()
                .status(HttpStatus.FOUND)
                .addHeader("Location", "/index.html")
                .build();
    }

    @RequestMapping(method = "POST", path = "/user/login")
    public static HttpResponse login(@RequestBody User user) {
        User existUser = Database.findUserById(user.getUserId());
        if (existUser == null || !existUser.getPassword().equals(user.getPassword()))
            return HttpResponse.builder()
                    .status(HttpStatus.FOUND)
                    .addHeader("Location", "/user/login_failed.html")
                    .build();

        String sessionId = SessionManager.generateSessionId();
        SessionManager.addSession(sessionId, user);

        return HttpResponse.builder()
                .status(HttpStatus.FOUND)
                .addHeader("Set-Cookie", "SID=" + sessionId + "; Path=/; HttpOnly")
                .addHeader("Location", "/index.html")
                .build();
    }
}
