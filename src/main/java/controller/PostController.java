package controller;

import db.Database;
import model.Request;
import model.Response;
import model.Session;
import model.User;
import service.UserService;

import java.util.UUID;

public class PostController {

    public static Response controlPost(Request request) {
        String statusCode = null;
        byte[] body = null;
        String mimeType = request.getMimeType();
        String redirectUrl = null;
        String sessionId = null;
        String base = "./src/main/resources";
        String path = request.getPath();
        String method = request.getMethod();
        Response response = new Response(statusCode, body, mimeType, redirectUrl, sessionId);

        if(path.equals("/user/create")) {
            User user = UserService.create(request.getBody());
            if (user != null) {
                response.setStatusCode("302");
                response.setRedirectUrl("/index.html");
            }
            else {
                response.setStatusCode("302");
                response.setRedirectUrl("/user/form.html");
            }
        }
        else if(path.equals("/user/login")) {
            String target = request.getBody().getOrDefault("userId", "");
            User user = Database.findUserById(target);
            if(user == null) {
                response.setStatusCode("302");
                response.setRedirectUrl("/user/login_failed.html");
            }
            else {
                if(user.getPassword().equals(request.getBody().getOrDefault("password", ""))) {
                    response.setStatusCode("302");
                    response.setRedirectUrl("/index.html");
                    sessionId = String.valueOf(UUID.randomUUID());
                    Session.addSession(sessionId, user);
                    response.setCookie(sessionId);
                }
                else {
                    response.setStatusCode("302");
                    response.setRedirectUrl("/user/login_failed.html");
                }
            }
        }

        return response;
    }
}
