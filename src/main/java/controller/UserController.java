package controller;

import annotation.PostMapping;
import annotation.RequestParam;
import db.Database;
import dto.Response;
import model.User;
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

        String sid = UUID.randomUUID().toString();

        return new Response.Builder()
                .httpStatus(HttpStatus.FOUND)
                .cookie(sid)
                .body("/index.html")
                .build();
    }

}

