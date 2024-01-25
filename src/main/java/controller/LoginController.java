package controller;

import db.Database;
import http.HttpRequest;
import http.HttpResponse;
import model.User;

import java.util.Map;

public class LoginController implements Controller{

    private final Database database;

    public LoginController() {
        database = Database.getInstance();
    }

    @Override
    public String process(HttpRequest request, HttpResponse response) {
        Map<String, String> data = request.getFormData();
        User user = database.findUserById(data.get("userId"));
        System.out.println(user.toString());
        if (data.get("password").equals(user.getPassword())) {
            return "redirect:/index";
        } else {
            return "redirect:/user/login_failed";
        }
    }
}
