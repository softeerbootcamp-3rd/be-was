package controller;

import db.Database;
import model.User;

import java.util.Map;

public class UserController implements Controller{

    private final Database database = Database.getInstance();

    @Override
    public String process(Map<String, String> paramMap) {
        User user = new User(paramMap.get("userId"), paramMap.get("password"), paramMap.get("name"), paramMap.get("email"));
        database.addUser(user);

        return "redirect:/user/login";
    }
}