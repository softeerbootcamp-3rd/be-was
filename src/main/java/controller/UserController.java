package controller;

import annotations.PostMapping;
import webserver.http.SessionManager;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import model.User;
import webserver.http.HttpStatus;
import service.UserService;

import java.io.IOException;
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
        Map<String, String> userProperties = request.getBody().convertRawStringAsMap();
        String userId = userProperties.getOrDefault("userId", "");
        String password = userProperties.getOrDefault("password", "");
        String name = userProperties.getOrDefault("name", "");
        String email = userProperties.getOrDefault("email", "");

        if (Stream.of(userId, password, name, email)
                .anyMatch(property -> property.isBlank())
        ) {
            response.setStatusCode(HttpStatus.FOUND);
            response.addHeaderProperty("Location", "/user/form_failed.html");
            return;
        }

        boolean isSaved = userService.saveUser(userId, password, name, email);

        if (isSaved) {
            response.setStatusCode(HttpStatus.FOUND);
            response.addHeaderProperty("Location", "/index.html");
        } else {
            response.setStatusCode(HttpStatus.FOUND);
            response.addHeaderProperty("Location", "/user/form_failed.html");
        }
    }

    @PostMapping(route = "/user/login")
    public void login(HttpRequest request, HttpResponse response) throws IOException {
        Map<String, String> userProperties = request.getBody().convertRawStringAsMap();
        String userId = userProperties.getOrDefault("userId", "");
        String password = userProperties.getOrDefault("password", "");

        User user = userService.getUser(userId, password);

        if (user == null) {
            response.setStatusCode(HttpStatus.FOUND);
            response.addHeaderProperty("Location", "/user/login_failed.html");
            return;
        }

        String sid = SessionManager.createSession(user);

        // 5분간 유지되는 세션을 생성하고 쿠키도 5분 후에 만료되도록 한다.
        response.setStatusCode(HttpStatus.FOUND);
        response.addHeaderProperty("Location", "/index.html");
        response.addHeaderProperty("Set-Cookie", "sid=" + sid + "; Path=/; Max-Age=300");
    }
}
