package controller.user;

import controller.ModelView;
import service.UserService;

import java.util.Map;

public interface UserController {

    UserService userService = new UserService();

    ModelView process(Map<String, String> paramMap);
}
