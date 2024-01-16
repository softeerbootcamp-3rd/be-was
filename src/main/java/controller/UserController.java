package controller;

import db.Database;
import dto.CreateUserDto;
import model.User;
import util.HttpRequest;
import util.ResponseBuilder;

import java.io.OutputStream;

public class UserController {

    public static void createUser(OutputStream out, HttpRequest request) {
        CreateUserDto dto = new CreateUserDto(request.getParamMap());
        User user = new User(dto.getUserId(), dto.getPassword(), dto.getName(), dto.getEmail());
        Database.addUser(user);
        ResponseBuilder.sendRedirect(out, "/index.html");
    }
}
