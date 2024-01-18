package controller.user;

import controller.ModelView;

import java.util.Map;

public class UserFormController implements UserController {
    @Override
    public ModelView process(Map<String, String> paramMap) {
        return new ModelView("user/form");
    }
}
