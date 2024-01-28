package controller;

import db.Database;
import db.SessionStorage;
import model.*;
import service.UserService;
import java.util.UUID;

public class UserController {

    public static void route(Request request, Response response, boolean login) {

        String path = request.getPath();

        if(request.getPath().equals("/user/list")) {
            if(login) {
                response.setStatusCode("302");
                response.addHeader("Location", "/user/list.html");
            }
            else {
                response.setStatusCode("302");
                response.addHeader("Location", "/user/login.html");
            }
            return;
        }

        if(request.getPath().equals("/user/create")) {
            UserInfo userInfo = new UserInfo(request.getBody());
            User user = UserService.create(userInfo);
            if (user != null) {
                response.setStatusCode("302");
                response.addHeader("Location", "/index.html");
            }
            else {
                response.setStatusCode("302");
                response.addHeader("Location", "/user/form.html");
            }
            return;
        }

        if(request.getPath().equals("/user/login")) {
            UserInfo userInfo = new UserInfo(request.getBody());
            String sessionId = UserService.login(userInfo);
            if(sessionId != null) {
                response.setStatusCode("302");
                response.addHeader("Location", "/index.html");
                response.addHeader("Set-Cookie", "sessionId=" + sessionId + "; Path=/; Max-Age=" + SessionStorage.SESSION_TIME);
            }
            else {
                response.setStatusCode("302");
                response.addHeader("Location", "/user/login_failed.html");
            }
            return;
        }

        if(request.getPath().equals("/user/logout")) {
            if(login) {
                response.setStatusCode("302");
                response.addHeader("Location", "/index.html");
                response.addHeader("Set-Cookie", "sessionId=; expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/; Secure; HttpOnly");
            }
            else {
                response.setStatusCode("302");
                response.addHeader("Location", "/user/login.html");
            }
            return;
        }

        LastController.route(request, response, login);
    }
}
