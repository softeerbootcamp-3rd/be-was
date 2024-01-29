package controller.user;

import controller.ModelView;
import exception.UserNotFoundException;
import model.Request;
import model.Response;
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
    public ModelView process(Request request, Response response) {
        try {
            response.set200Ok();

            String userId = request.getCookie("sid");
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
