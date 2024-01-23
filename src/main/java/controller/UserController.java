package controller;

import db.Database;
import http.request.Request;
import http.response.Response;
import model.User;

import java.util.Map;

import static util.UrlParser.parseQueryString;

public class UserController extends Controller {
    @Override
    public void doPost(Request request, Response response) {
        Map<String, String> queryParams = parseQueryString(request.getBody());
        User user = new User(queryParams.get("userId"),
                queryParams.get("password"), queryParams.get("name"), queryParams.get("email"));
        Database.addUser(user);

        response.redirect("/index.html");
    }
}