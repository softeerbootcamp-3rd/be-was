package controller.user;

import controller.ModelView;
import exception.LoginFailedException;
import model.Response;

import java.util.HashMap;
import java.util.Map;

public class UserLoginController implements UserController{
    @Override
    public ModelView process(Map<String, String> paramMap, Response response) {
        String userId = paramMap.get("userId");
        String password = paramMap.get("password");

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
        response.putToHeaderMap("Set-Cookie", "sid=" + userId + "; Path=/; Max-Age=30");

        return new ModelView(path);
    }
}
