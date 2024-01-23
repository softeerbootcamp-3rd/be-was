package controller;

import db.Database;
import model.User;

import java.util.Map;

public class UserController implements Controller{

    private final Database database = Database.getInstance();

    @Override
    public String process(Map<String, String> map) {
        User user = new User(map.get("userId"), map.get("password"), map.get("name"), map.get("email"));
        database.addUser(user);

        return "redirect:/user/login";
    }
}