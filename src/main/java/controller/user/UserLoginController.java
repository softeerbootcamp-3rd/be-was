package controller.user;

import controller.ModelView;

import java.util.Map;

public class UserLoginController implements UserController {
    @Override
    public ModelView process(Map<String, String> paramMap) {
        return new ModelView("/templates/user/login.html");
    }
}
