package controller;

import annotation.PostMapping;
import db.Database;
import http.HttpRequest;
import http.HttpResponse;
import model.User;

import java.util.Map;

public class UserController implements Controller{

    private final Database database = Database.getInstance();

    @Override
    @PostMapping("/user/create")
    public String process(HttpRequest request, HttpResponse response) {
        Map<String, String> data = request.getFormData();
        User user = new User(data.get("userId"), data.get("password"), data.get("name"), data.get("email"));
        database.addUser(user);

        return "redirect:/index";
    }
}