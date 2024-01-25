package controller;

import annotations.PostMapping;
import http.HttpRequest;
import http.HttpResponse;
import http.header.ResponseHeader;
import model.User;
import http.status.HttpStatus;
import service.UserService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class UserController {
    public static UserController getInstance() {
        return LazyHolder.INSTANCE;
    }

    private static class LazyHolder {
        private static final UserController INSTANCE = new UserController();
    }

    private final UserService userService = UserService.getInstance();

    @PostMapping(route = "/user/create")
    public void signup(HttpRequest request, HttpResponse response) throws IOException {
        Map<String, String> userProperties = request.getRequestBody().convertRawStringAsMap();
        String userId = userProperties.getOrDefault("userId", "");
        String password = userProperties.getOrDefault("password", "");
        String name = userProperties.getOrDefault("name", "");
        String email = userProperties.getOrDefault("email", "");

        Map<String, String> headerProperties = new HashMap<>();

        if (Stream.of(userId, password, name, email)
                .anyMatch(e->e.isBlank())
        ) {
            response.setHeader(
                    ResponseHeader.of(HttpStatus.BAD_REQUEST, headerProperties)
            );

            return;
        }

        boolean isSaved = userService.saveUser(new User(userId, password, name, email));

        if (isSaved) {
            headerProperties.put("Location", "/user/login.html");

            response.setHeader(
                    ResponseHeader.of(HttpStatus.FOUND, headerProperties)
            );
        } else {
            headerProperties.put("Location", "/user/form_failed.html");

            response.setHeader(
                    ResponseHeader.of(HttpStatus.FOUND, headerProperties)
            );
        }
    }
}
