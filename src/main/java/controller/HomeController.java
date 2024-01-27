package controller;

import exception.FileNotFoundExceptionHandler;
import model.User;
import util.ResourceUtils;
import util.SessionManager;
import util.http.*;

import java.io.FileNotFoundException;
import java.io.IOException;

public class HomeController {
    private final HttpRequest httpRequest;

    public HomeController(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    public ResponseEntity<?> run() throws IOException {
        try {
            String lastPath = ResourceUtils.getLastPath(httpRequest.getPath());

            if ("index.html".equals(lastPath) || "/".equals(lastPath))
                return home();

            byte[] body = ResourceUtils.getStaticResource(httpRequest.getPath());
            String contentType = httpRequest.getHeader(HttpHeaders.ACCEPT);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, contentType)
                    .header(HttpHeaders.CONTENT_LENGTH, Integer.toString(body.length))
                    .body(body);
        } catch (FileNotFoundException e) {
            return FileNotFoundExceptionHandler.handle();
        }
    }

    private ResponseEntity<?> home() throws IOException {
        boolean isLoggedIn = SessionManager.isLoggedIn(httpRequest);

        if (!isLoggedIn) {
            byte[] body = ResourceUtils.getStaticResource(httpRequest.getPath());
            String contentType = httpRequest.getHeader(HttpHeaders.ACCEPT);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, contentType)
                    .header(HttpHeaders.CONTENT_LENGTH, Integer.toString(body.length))
                    .body(body);
        }

        String line = "<li><a role=\"button\">{{userId}}</a></li>";
        User user = SessionManager.getLoggedInUser(httpRequest);
        line = line.replace("{{userId}}", user.getUserId());
        String html = new String(ResourceUtils.getStaticResource(httpRequest.getPath()));
        byte[] body =  html.replace("<li><a href=\"user/login.html\" role=\"button\">로그인</a></li>", line).getBytes();

        String contentType = httpRequest.getHeader(HttpHeaders.ACCEPT);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .header(HttpHeaders.CONTENT_LENGTH, Integer.toString(body.length))
                .body(body);
    }
}
