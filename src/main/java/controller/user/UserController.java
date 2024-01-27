package controller.user;

import controller.ModelView;
import model.Response;
import service.UserService;

import java.util.Map;

public interface UserController {

    UserService userService = new UserService();

    ModelView process(Map<String, String> paramMap, Response response);
}
