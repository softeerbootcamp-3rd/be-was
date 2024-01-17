package controller;

import db.Database;
import dto.CreateUserDto;
import model.User;
import webserver.HttpRequest;
import webserver.HttpResponse;
import webserver.HttpStatus;

public class UserController {

    public static HttpResponse createUser(HttpRequest request) {
        CreateUserDto dto = new CreateUserDto(request.getParamMap());
        User user = new User(dto.getUserId(), dto.getPassword(), dto.getName(), dto.getEmail());
        Database.addUser(user);
        return HttpResponse.builder()
                .status(HttpStatus.FOUND)
                .addHeader("Location", "/index.html")
                .build();
    }
}
