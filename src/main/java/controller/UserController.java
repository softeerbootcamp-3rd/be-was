package controller;

import annotation.Controller;
import annotation.RequestBody;
import annotation.RequestMapping;
import annotation.RequestParam;
import db.Database;
import model.User;
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
}
