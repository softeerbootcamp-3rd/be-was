package controller;

import db.Database;
import model.*;
import service.UserService;
import java.util.UUID;

public class UserController {

    public static void route(Request request, Response response) {

        String path = request.getPath();

        if(request.getPath().equals("/user/list")) {
            if(request.getSessionId() != null) {
                response.setStatusCode("302");
                response.setRedirectUrl("/user/list.html");
            }
            else {
                response.setStatusCode("302");
                response.setRedirectUrl("/user/login.html");
            }
            return;
        }

        if(request.getPath().equals("/user/create")) {
            UserInfo userInfo = new UserInfo(request.getBody());
            User user = UserService.create(userInfo);
            if (user != null) {
                response.setStatusCode("302");
                response.setRedirectUrl("/index.html");
            }
            else {
                response.setStatusCode("302");
                response.setRedirectUrl("/user/form.html");
            }
            return;
        }

        if(request.getPath().equals("/user/login")) {
            UserInfo userInfo = new UserInfo(request.getBody());
            String sessionId = UserService.login(userInfo);
            if(sessionId != null) {
                response.setStatusCode("302");
                response.setRedirectUrl("/index.html");
                response.setCookie(sessionId);
            }
            else {
                response.setStatusCode("302");
                response.setRedirectUrl("/user/login_failed.html");
            }
            return;
        }

        LastController.route(request, response);
    }
}
