package controller;

import db.Database;
import httpmessage.HttpSession;
import httpmessage.request.HttpRequest;
import httpmessage.response.HttpResponse;
import model.User;
import java.io.IOException;

public class UserLoginController implements Controller {
    public void service(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        String userId = httpRequest.getParmeter("userId");
        String password = httpRequest.getParmeter("password");

        User user = Database.findUserById(userId);

        httpResponse.setStatusCode(302);

        if (user != null && user.getPassword().equals(password)) {
            // 로그인 성공
            HttpSession session = new HttpSession(user);

            httpResponse.setSid(session.getId());

            httpResponse.setPath("/index.html");
        } else {
            // 로그인 실패
            httpResponse.setPath("/user/login_failed.html");
        }
    }
}
