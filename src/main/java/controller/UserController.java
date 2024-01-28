package controller;

import dto.UserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import webserver.annotation.GetMapping;
import webserver.annotation.PostMapping;
import webserver.annotation.RequestBody;
import webserver.annotation.RequestParam;
import webserver.response.Response;

public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService = new UserService();

    @GetMapping(path = "/user/create")
    public Response createUser(@RequestParam(name = "userId") String userId,
                               @RequestParam(name = "password") String password,
                               @RequestParam(name = "name") String name,
                               @RequestParam(name = "email") String email) {
        logger.debug("createUser() 실행");

        userService.createUser(userId, password, name, email);

        return Response.redirect("/index.html");
    }

    @PostMapping(path = "/user/create")
    public Response createUserByPost(@RequestBody UserRequest.Register register){
        userService.createUser(register);

        return Response.redirect("/index.html");
    }
}
