package controller.user;

import constant.HttpStatus;
import controller.ModelView;
import exception.LoginFailedException;
import model.HttpRequest;
import model.HttpResponse;
import model.User;
import service.UserService;
import util.SessionManager;

public class UserLoginController implements UserController {
    private final UserService userService;

    public UserLoginController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ModelView process(HttpRequest httpRequest, HttpResponse httpResponse) {
        String userId = httpRequest.getParameter("userId");
        String password = httpRequest.getParameter("password");

        String path = "";
        User loginUser = null;
        try {
            loginUser = userService.login(userId, password);
        } catch (LoginFailedException e) {
            path = "/user/login_failed.html";
            httpResponse.addHeader("Location", path);

            e.printStackTrace();

            return new ModelView(path, HttpStatus.FOUND);
        }

        // 로그인 성공
        String sid = SessionManager.generateSID();
        SessionManager.addSession(sid, loginUser);

        path = "/index.html";
        httpResponse.addHeader("Location", path);
        httpResponse.addHeader("Set-Cookie", "sid=" + sid + "; Path=/; Max-Age=1800" + SessionManager.EXPIRED_TIME);

        return new ModelView(path, HttpStatus.FOUND);
    }
}
