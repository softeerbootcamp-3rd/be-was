package controller;

import exception.FileNotFoundExceptionHandler;
import model.User;
import service.UserService;
import util.ResourceUtils;
import util.SessionManager;
import util.http.HttpHeaders;
import util.http.HttpStatus;
import util.http.HttpRequest;
import util.http.ResponseEntity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.Map;

public class UserController {
    private final HttpRequest httpRequest;
    private final UserService userService;

    public UserController(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
        this.userService = new UserService();
    }

    public ResponseEntity<?> run() throws IOException {
        try {
            String lastPath = ResourceUtils.getLastPath(httpRequest.getPath());

            ResponseEntity<?> responseEntity = null;
            if (lastPath.equals("create")) {
                responseEntity = create();
            }
            if (lastPath.equals("login")) {
                responseEntity = login();
            }
            if (lastPath.equals("list")) {
                responseEntity = list();
            }
            return responseEntity;
        } catch (FileNotFoundException e) {
            return FileNotFoundExceptionHandler.handle();
        }
    }

    private ResponseEntity<?> create() {
//        Map<String, String> query = httpRequest.getQueryMap();
        Map<String, String> query = httpRequest.getBodyParams();

        String userId = query.get("userId");
        String password = query.get("password");
        String name = query.get("name");
        String email = query.get("email");

        userService.create(userId, password, name, email);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create("/index.html"))
                .build();
    }

    private ResponseEntity<?> login() {
        Map<String, String> query = httpRequest.getBodyParams();

        String userId = query.get("userId");
        String password = query.get("password");
        String sessionId = userService.login(userId, password);

        if (sessionId == null) {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create("/user/login_failed.html"))
                    .build();
        } else {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header(HttpHeaders.SET_COOKIE, "SID=" + sessionId + "; Path=/")
                    .location(URI.create("/index.html"))
                    .build();
        }
    }

    private ResponseEntity<?> list() throws IOException {
        boolean isLoggedIn = SessionManager.isLoggedIn(httpRequest);

        if (!isLoggedIn) {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create("/user/login.html"))
                    .build();
        }

        StringBuilder sb = new StringBuilder();
        String line = "<tr><th scope=\"row\">1</th> <td>{{userId}}</td> <td>{{name}}</td> <td>{{email}}</td><td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td></tr>";
        Collection<User> userlist = userService.list();
        for (User user : userlist) {
            sb.append(line.replace("{{userId}}", user.getUserId())
                    .replace("{{name}}", user.getName())
                    .replace("{{email}}", user.getEmail()));
        }
        String html = new String(ResourceUtils.getStaticResource("/user/list.html"));
        byte[] body =  html.replace("<tr id=\"replace\"></tr>", sb.toString()).getBytes();

        String contentType = httpRequest.getHeader(HttpHeaders.ACCEPT);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .header(HttpHeaders.CONTENT_LENGTH, Integer.toString(body.length))
                .body(body);
    }
}