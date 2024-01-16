package controller;

import db.Database;
import dto.CreateUserDto;
import model.User;
import util.HttpRequest;
import util.HttpResponse;
import util.HttpStatus;
import util.ResponseBuilder;

import java.io.OutputStream;

public class UserController {

    public static HttpResponse createUser(HttpRequest request) {
        CreateUserDto dto = new CreateUserDto(request.getParamMap());
        User user = new User(dto.getUserId(), dto.getPassword(), dto.getName(), dto.getEmail());
        Database.addUser(user);
        return new HttpResponse(HttpStatus.FOUND, "/index.html");
    }
}
