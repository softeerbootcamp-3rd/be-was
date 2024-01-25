package controller;

import service.UserService;
import util.http.HttpHeaders;
import util.http.HttpStatus;
import util.http.HttpRequest;
import util.http.ResponseEntity;

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
                    .location(URI.create("/index.html"))
                    .build();
            return responseEntity;
        }
        if (lastPath.equals("login")) {
            String sessionId = login();
            if (sessionId == null) {
                responseEntity = ResponseEntity.status(HttpStatus.FOUND)
                        .location(URI.create("/user/login_failed.html"))
                        .build();
            } else {
                responseEntity = ResponseEntity.status(HttpStatus.FOUND)
                        .header(HttpHeaders.SET_COOKIE, "SID=" + sessionId + "; Path=/")
                        .location(URI.create("/index.html"))
                        .build();
            }
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

    private String login() {
        Map<String, String> query = httpRequest.getBodyParams();

        String userId = query.get("userId");
        String password = query.get("password");
        return userService.login(userId, password);
    }
}