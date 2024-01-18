package controller;

import annotation.GetMapping;
import annotation.RequestParam;
import db.Database;
import dto.ResponseBuilder;
import model.User;
import webserver.HttpStatus;


public class GetController {

    @GetMapping(path = "/user/create")
    public static ResponseBuilder signup(@RequestParam(name = "userId") String userId,
                                         @RequestParam(name = "password") String password,
                                         @RequestParam(name = "name") String name,
                                         @RequestParam(name = "email") String email) {

        User user = new User(userId, password, name, email);
        Database.addUser(user);

        return new ResponseBuilder.Builder()
                .httpStatus(HttpStatus.FOUND)
                .body("/index.html")
                .build();
    }

}

