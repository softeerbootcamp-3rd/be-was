package controller;

import annotation.GetMapping;
import annotation.RequestParam;
import exception.GeneralException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import webserver.response.Response;
import webserver.type.ContentType;

import java.io.Serializable;

public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService = new UserService();

    @GetMapping(path = "/user/create")
    public Response createUser(@RequestParam(name = "userId") String userId,
                               @RequestParam(name = "password") String password,
                               @RequestParam(name = "name") String name,
                               @RequestParam(name = "email") String email) throws GeneralException {
        logger.debug("createUser() 실행");

        userService.createUser(userId, password, name, email);

        return Response.onSuccess(ContentType.HTML, "회원가입 완료".getBytes());
    }
}
