package controller;

import db.Database;
import model.User;
import util.ResponseBuilder;
import util.URIParser;

import java.io.OutputStream;
import java.util.Map;

public class UserController {

    public static void createUser(OutputStream out, String requestPath) {
        Map<String, String> paramMap = URIParser.parseQueryString(URIParser.extractQuery(requestPath));
        User user = new User(paramMap.get("userId"), paramMap.get("password"),
                paramMap.get("name"), paramMap.get("email"));
        Database.addUser(user);
        ResponseBuilder.sendRedirect(out, "/index.html");
    }
}
