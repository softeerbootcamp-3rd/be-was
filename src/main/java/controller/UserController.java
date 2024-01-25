package controller;

import service.UserService;
import util.http.HttpStatus;
import util.http.HttpRequest;
import util.http.ResponseEntity;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

public class UserController {
    private final HttpRequest httpRequest;
    private final UserService userService;

    public UserController(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
        this.userService = new UserService();
    }

    public ResponseEntity<?> run() {
        String path = httpRequest.getPath();

        String[] tokens = path.split("/");
        String lastPath = tokens[tokens.length - 1];

        ResponseEntity<?> responseEntity;
        if (lastPath.equals("create")) {
            String userId = create();
            responseEntity = ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create("/"))
                    .build();
            return responseEntity;
        }
        return null;
    }

    private String create() {
//        Map<String, String> query = httpRequest.getQueryMap();
        Map<String, String> query = httpRequest.getBodyParams();

        String userId = query.get("userId");
        String password = query.get("password");
        String name = query.get("name");
        String email = query.get("email");

        return userService.create(userId, password, name, email);
    }
}