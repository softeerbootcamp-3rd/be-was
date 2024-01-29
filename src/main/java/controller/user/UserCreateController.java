package controller.user;

import controller.ModelView;
import exception.AlreadyExistUserException;
import model.Request;
import model.Response;
import service.UserService;

import java.util.HashMap;
import java.util.Map;

public class UserCreateController implements UserController {
    private final UserService userService;

    public UserCreateController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ModelView process(Request request, Response response) {
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String email = request.getParameter("email");

        response.set302Redirect();
        String path = "";

        try {
            userService.addUser(userId, password, name, email);
        } catch (AlreadyExistUserException e) {
            path = "/user/form_failed.html";
            response.putToHeaderMap("Location", path);

            return new ModelView(path);
        }

        // 회원가입 성공
        path = "/index.html";
        response.putToHeaderMap("Location", path);

        return new ModelView(path);
    }
}
