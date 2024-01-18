package controller;

import annotation.Controller;
import annotation.RequestMapping;
import annotation.RequestParam;
import db.Database;
import model.User;
import webserver.HttpResponse;
import webserver.HttpStatus;

@Controller
public class UserController {

    @RequestMapping(method = "GET", path = "/user/create")
    public static HttpResponse createUser(@RequestParam("request") String userId,
                                          @RequestParam("password") String password,
                                          @RequestParam("name") String name,
                                          @RequestParam("email") String email) {
        User user = new User(userId, password, name, email);
        Database.addUser(user);
        return HttpResponse.builder()
                .status(HttpStatus.FOUND)
                .addHeader("Location", "/index.html")
                .build();
    }
}
