package controller;

import db.BoardDatabase;
import exception.FileNotFoundExceptionHandler;
import model.User;
import util.template.IndexTemplate;
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
            String path = httpRequest.getPath();
//            String lastPath = ResourceUtils.getLastPath(httpRequest.getPath());

            if ("/index.html".equals(path) || "/".equals(path))
                return home();

            byte[] body = ResourceUtils.getStaticResource(httpRequest.getPath());

            return ResponseEntity.ok()
                    .contentType(MediaType.getContentType(httpRequest))
                    .contentLength(body.length)
                    .body(body);
        } catch (FileNotFoundException e) {
            return FileNotFoundExceptionHandler.handle();
        }
    }

    private ResponseEntity<?> home() throws IOException {
        User loggedInUser = SessionManager.getLoggedInUser(httpRequest);

        byte[] body = IndexTemplate.render(loggedInUser, BoardDatabase.findAll());

        return ResponseEntity.ok()
                .contentType(MediaType.getContentType(httpRequest))
                .contentLength(body.length)
                .body(body);
    }
}
