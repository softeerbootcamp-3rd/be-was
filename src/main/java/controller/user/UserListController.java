package controller.user;

import constant.HttpStatus;
import controller.ModelView;
import exception.UserNotFoundException;
import model.HttpRequest;
import model.HttpResponse;
import model.User;
import service.UserService;
import util.SessionManager;

import java.util.Collection;
import java.util.HashMap;


public class UserListController implements UserController {
    private final UserService userService;

    public UserListController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ModelView process(HttpRequest httpRequest, HttpResponse httpResponse) {
        String sid = httpRequest.getCookie("sid");

        if (sid == null || !SessionManager.isLoggedIn(sid)) {
            return new ModelView("/user/login.html", HttpStatus.OK);
        }

        Collection<User> users = userService.findAll();
        HashMap<String, Object> model = new HashMap<>();
        model.put("{{user-list}}", users);

        return new ModelView("/user/list.html", model, HttpStatus.OK);
    }
}
