package controller.user;

import controller.ModelView;
import exception.LoginFailedException;
import model.Request;
import model.Response;

public class UserLoginController implements UserController{
    @Override
    public ModelView process(Request request, Response response) {
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");

        response.set302Redirect();
        String path = "";

        try {
            userService.login(userId, password);
        } catch (LoginFailedException e) {
            path = "/user/login_failed.html";
            response.putToHeaderMap("Location", path);

            e.printStackTrace();

            return new ModelView(path);
        }

        // 로그인 성공
        path = "/index.html";
        response.putToHeaderMap("Location", path);
        response.putToHeaderMap("Set-Cookie", "sid=" + userId + "; Path=/; Max-Age=1800");

        return new ModelView(path);
    }
}
