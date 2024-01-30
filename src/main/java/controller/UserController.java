package controller;

import exception.*;
import model.User;
import service.UserService;
import util.template.IndexTemplate;
import util.ResourceUtils;
import util.SessionManager;
import util.http.*;

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
                if (httpRequest.getMethod() == HttpMethod.POST)
                    responseEntity = create();
                else
                    throw new WebException(HttpStatus.METHOD_NOT_ALLOWED);
            }
            if (lastPath.equals("login")) {
                if (httpRequest.getMethod() == HttpMethod.POST)
                    responseEntity = login();
                else
                    throw new WebException(HttpStatus.METHOD_NOT_ALLOWED);
            }
            if (lastPath.equals("list")) {
                responseEntity = list();
            }
            if (lastPath.equals("profile")) {
                responseEntity = profile();
            }
            if (lastPath.equals("logout")) {
                responseEntity = logout();
            }
            if (lastPath.equals("update")) {
                if (httpRequest.getMethod() == HttpMethod.GET)
                    responseEntity = getUpdate();
                if (httpRequest.getMethod() == HttpMethod.POST)
                    responseEntity = postUpdate();
            }
            return responseEntity;
        } catch (WebException e) {
            return WebExceptionHandler.handle(e);
        } catch (FileNotFoundException e) {
            return FileNotFoundExceptionHandler.handle();
        }
    }

    private ResponseEntity<?> create() throws IOException {
        Map<String, String> query = httpRequest.getBodyParams();

        String userId = query.get("userId");
        String password = query.get("password");
        String name = query.get("name");
        String email = query.get("email");

        try {
            userService.create(userId, password, name, email);
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create("/index.html"))
                    .build();
        } catch (UserException e) {
            return UserExceptionHandler.handle(e);
        }
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
        String tr = "<tr><th scope=\"row\">1</th> <td>{{userId}}</td> <td>{{name}}</td> <td>{{email}}</td><td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td></tr>";

        Collection<User> users = userService.list();
        for (User user : users) {
            sb.append(tr.replace("{{userId}}", user.getUserId())
                    .replace("{{name}}", user.getName())
                    .replace("{{email}}", user.getEmail()));
        }

        User loggedInUser = SessionManager.getLoggedInUser(httpRequest);

        String html = new String(ResourceUtils.getStaticResource("/user/list.html"));
        byte[] body =  html.replace("<tr id=\"users\"></tr>", sb.toString())
                .replace("<li>userId</li>", IndexTemplate.USERNAME.replace("{}", loggedInUser.getName()))
                .getBytes();

        return ResponseEntity.ok()
                .contentType(MediaType.getContentType(httpRequest))
                .contentLength(body.length)
                .body(body);
    }

    private ResponseEntity<?> profile() throws IOException {
        boolean isLoggedIn = SessionManager.isLoggedIn(httpRequest);

        if (!isLoggedIn) {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create("/user/login.html"))
                    .build();
        }

        User loggedInUser = SessionManager.getLoggedInUser(httpRequest);

        String name = "<h4 id=\"username\" class=\"media-heading\">{{name}}</h4>";
        name = name.replace("{{name}}", loggedInUser.getName());

        String email = "<a id=\"email\" href=\"#\" class=\"btn btn-xs btn-default\"><span class=\"glyphicon glyphicon-envelope\"></span>&nbsp;{{email}}</a>";
        email = email.replace("{{email}}", loggedInUser.getEmail());

        String html = new String(ResourceUtils.getStaticResource("/user/profile.html"));
        byte[] body =  html.replace("<li>userId</li>", IndexTemplate.USERNAME.replace("{}", loggedInUser.getName()))
                .replace("<h4 id=\"username\" class=\"media-heading\">자바지기</h4>", name)
                .replace("<a id=\"email\" href=\"#\" class=\"btn btn-xs btn-default\"><span class=\"glyphicon glyphicon-envelope\"></span>&nbsp;javajigi@slipp.net</a>", email)
                .getBytes();

        return ResponseEntity.ok()
                .contentType(MediaType.getContentType(httpRequest))
                .contentLength(body.length)
                .body(body);
    }

    private ResponseEntity<?> logout() {
        boolean isLoggedIn = SessionManager.isLoggedIn(httpRequest);

        if (isLoggedIn) {
            SessionManager.removeSession(httpRequest);
        }

        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.SET_COOKIE, "SID=; Path=/; Max-Age=0")
                .location(URI.create("/index.html"))
                .build();
    }

    private ResponseEntity<?> getUpdate() throws IOException {
        boolean isLoggedIn = SessionManager.isLoggedIn(httpRequest);

        if (!isLoggedIn) {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create("/user/login.html"))
                    .build();
        }

        User loggedInUser = SessionManager.getLoggedInUser(httpRequest);

        String input = "<input class=\"form-control\" id=\"userId\" name=\"userId\" placeholder=\"{{userId}}\" disabled>";
        input = input.replace("{{userId}}", loggedInUser.getUserId());

        String html = new String(ResourceUtils.getStaticResource("/user/update.html"));
        byte[] body = html.replace("<input class=\"form-control\" id=\"userId\" name=\"userId\" placeholder=\"User ID\">", input)
                .replace("<li>userId</li>", IndexTemplate.USERNAME.replace("{}", loggedInUser.getName()))
                .getBytes();

        return ResponseEntity.ok()
                .contentType(MediaType.getContentType(httpRequest))
                .contentLength(body.length)
                .body(body);
    }

    private ResponseEntity<?> postUpdate() throws IOException {
        User loggedInUser = SessionManager.getLoggedInUser(httpRequest);

        Map<String, String> query = httpRequest.getBodyParams();

        String userId = loggedInUser.getUserId();
        String password = query.get("password");
        String name = query.get("name");
        String email = query.get("email");

        try {
            userService.update(userId, password, name, email);
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create("/index.html"))
                    .build();
        } catch (UserException e) {
            return UserExceptionHandler.handle(e, httpRequest);
        }
    }
}