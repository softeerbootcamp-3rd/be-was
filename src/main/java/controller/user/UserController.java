package controller.user;

import controller.ModelView;
import model.Request;
import model.Response;
import service.UserService;

import java.util.Map;

public interface UserController {

    UserService userService = new UserService();

    ModelView process(Request request, Response response);
}
