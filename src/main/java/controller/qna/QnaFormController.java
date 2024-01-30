package controller.qna;

import constant.HttpStatus;
import controller.ModelView;
import exception.UserNotFoundException;
import model.HttpRequest;
import model.HttpResponse;
import model.User;
import service.UserService;

public class QnaFormController implements QnaController{
    private final UserService userService;

    public QnaFormController(UserService userService) {
        this.userService = userService;
    }
    @Override
    public ModelView process(HttpRequest httpRequest, HttpResponse httpResponse) {
        try {
            String userId = httpRequest.getCookie("sid");
            User findUser = userService.findUserById(userId);
        } catch (IllegalArgumentException | UserNotFoundException e) {
            e.printStackTrace();
            return new ModelView("/user/login.html", HttpStatus.OK);
        }

        // 로그인 상태일 경우
        return new ModelView("/qna/form.html", HttpStatus.OK);
    }
}
