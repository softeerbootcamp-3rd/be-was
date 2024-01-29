package controller.user;

import controller.ModelView;
import exception.LoginFailedException;
import model.HttpRequest;
import model.HttpResponse;
import service.UserService;

public class UserLoginController implements UserController{
    private final UserService userService;

    public UserLoginController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ModelView process(HttpRequest httpRequest, HttpResponse httpResponse) {
        String userId = httpRequest.getParameter("userId");
        String password = httpRequest.getParameter("password");

        httpResponse.set302Redirect();
        String path = "";

        try {
            userService.login(userId, password);
        } catch (LoginFailedException e) {
            path = "/user/login_failed.html";
            httpResponse.putToHeaderMap("Location", path);

            e.printStackTrace();

            return new ModelView(path);
        }

        // 로그인 성공
        path = "/index.html";
        httpResponse.putToHeaderMap("Location", path);
        httpResponse.putToHeaderMap("Set-Cookie", "sid=" + userId + "; Path=/; Max-Age=1800");

        return new ModelView(path);
    }
}
