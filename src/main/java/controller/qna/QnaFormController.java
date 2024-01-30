package controller.qna;

import constant.HttpStatus;
import controller.ModelView;
import exception.UserNotFoundException;
import model.HttpRequest;
import model.HttpResponse;
import model.User;
import service.UserService;
import util.SessionManager;

public class QnaFormController implements QnaController {
    private final UserService userService;

    public QnaFormController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ModelView process(HttpRequest httpRequest, HttpResponse httpResponse) {
        String sid = httpRequest.getCookie("sid");

        if (!SessionManager.isLoggedIn(sid)) {
            return new ModelView("/user/login.html", HttpStatus.OK);

        }

        return new ModelView("/qna/form.html", HttpStatus.OK);
    }
}
