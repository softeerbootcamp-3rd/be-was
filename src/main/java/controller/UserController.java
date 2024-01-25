package controller;

import db.Database;
import http.request.Request;
import http.response.Response;
import model.User;

import java.util.Map;

import static util.Parser.parseQueryString;

public class UserController extends Controller {
    @Override
    public void doPost(Request request, Response response) {
        try{
            Map<String, String> queryParams = parseQueryString(request.getBody());
            validate(queryParams);
            User user = new User(queryParams.get("userId"),
                    queryParams.get("password"), queryParams.get("name"), queryParams.get("email"));
            Database.addUser(user);

            response.redirect("/index.html");
        }
        catch (IllegalArgumentException e){
            response.redirect("/user/form_failed.html");
        }
    }

    private void validate(Map<String, String> params){
        if (!params.containsKey("userId") ||
            !params.containsKey("password") ||
            !params.containsKey("name") ||
            !params.containsKey("email")) {
            throw new IllegalArgumentException("Missing required parameters.");
        }
    }
}