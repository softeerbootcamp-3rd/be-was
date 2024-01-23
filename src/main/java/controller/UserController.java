package controller;

import db.Database;
import http.request.Request;
import http.response.Response;
import model.User;

public class UserController extends Controller{

    @Override
    public void doGet(Request request, Response response) {
        // TO DO: 회원 가입
        User user = new User("1", "1", "rr", "rr@ss");
        Database.addUser(user);

//        response.
    }

    @Override
    public void doPost(Request request, Response response) {

    }
}