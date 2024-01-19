package controller;

import annotation.Controller;
import annotation.RequestMapping;
import annotation.RequestParam;
import db.Database;
import model.User;
import webserver.HttpResponse;
import constant.HttpStatus;

@Controller
public class UserController {

    @RequestMapping(method = "GET", path = "/user/create")
    public static HttpResponse createUser(@RequestParam(value = "userId", required = true) String userId,
                                          @RequestParam(value = "password", required = true) String password,
                                          @RequestParam("name") String name,
                                          @RequestParam("email") String email) {

        User user = new User(userId, password, name, email);
        User existUser = Database.findUserById(userId);
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
}
