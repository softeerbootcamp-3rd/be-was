package controller.user;

import controller.ModelView;
import exception.UserNotFoundException;
import model.HttpRequest;
import model.HttpResponse;
import model.User;
import service.UserService;

import java.util.Collection;
import java.util.HashMap;


public class UserListController implements UserController {
    private final UserService userService;

    public UserListController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ModelView process(HttpRequest httpRequest, HttpResponse httpResponse) {
        try {
            httpResponse.set200Ok();

            String userId = httpRequest.getCookie("sid");
            User findUser = userService.findUserById(userId);
        } catch (IllegalArgumentException | UserNotFoundException e) {
            e.printStackTrace();
            return new ModelView("/templates/user/login.html");
        }

        Collection<User> users = userService.findAll();
        HashMap<String, Object> model = new HashMap<>();
        model.put("{{user-list}}", users);

        return new ModelView("/templates/user/list.html", model);
    }
}
