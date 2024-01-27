package controller.user;

import controller.ModelView;
import exception.AlreadyExistUserException;
import model.Response;

import java.util.HashMap;
import java.util.Map;

public class UserCreateController implements UserController {

    @Override
    public ModelView process(Map<String, String> paramMap, Response response) {
        String userId = paramMap.get("userId");
        String password = paramMap.get("password");
        String name = paramMap.get("name");
        String email = paramMap.get("email");

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
