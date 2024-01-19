package controller.user;

import controller.ModelView;
import exception.AlreadyExistUserException;
import service.UserService;

import java.util.Map;

public class UserCreateController implements UserController {

    @Override
    public ModelView process(Map<String, String> paramMap) {
        String userId = paramMap.get("userId");
        String password = paramMap.get("password");
        String name = paramMap.get("name");
        String email = paramMap.get("email");

        try {
            userService.addUser(userId, password, name, email);
        } catch (AlreadyExistUserException e) {
            return new ModelView("/templates/user/form_failed.html");
        }

        return new ModelView("/templates/index.html");
    }
}
