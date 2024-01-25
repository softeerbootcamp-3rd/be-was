package controller;

import db.Database;
import http.HttpRequest;
import http.HttpResponse;
import model.User;

import java.util.Map;
import java.util.UUID;

public class LoginController implements Controller{

    private final Database database;

    public LoginController() {
        database = Database.getInstance();
    }

    @Override
    public String process(HttpRequest request, HttpResponse response) {
        Map<String, String> data = request.getFormData();
        User user = database.findUserById(data.get("userId"));

        if (data.get("password").equals(user.getPassword())) {
            String sid = generateSessionId();
            response.setHeader("Set-Cookie","sid="+sid+"; Path=/");
            SessionManager.addSession(sid, user);
            return "redirect:/index";
        } else {
            return "redirect:/user/login_failed";
        }
    }

    private String generateSessionId() {
        return UUID.randomUUID().toString();
    }
}
