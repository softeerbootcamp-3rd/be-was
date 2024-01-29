package controller;

import exception.*;
import model.User;
import service.BoardService;
import util.ResourceUtils;
import util.SessionManager;
import util.StringUtils;
import util.http.*;
import util.template.ShowTemplate;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.Map;

public class BoardController {
    private final HttpRequest httpRequest;
    private final BoardService boardService;
    public BoardController(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
        this.boardService = new BoardService();
    }

    public ResponseEntity<?> run() throws IOException {
        try {
            String path = httpRequest.getPath();
            String lastPath = ResourceUtils.getLastPath(httpRequest.getPath());

            ResponseEntity<?> responseEntity = null;
            if (lastPath.equals("write.html")) {
                responseEntity = writeForm();
            }
            if (lastPath.equals("write")) {
                if (httpRequest.getMethod() == HttpMethod.POST)
                    responseEntity = write();
                else
                    throw new MethodNotAllowedException();
            }
            if (lastPath.equals("comment")) {
                if (httpRequest.getMethod() == HttpMethod.POST)
                    responseEntity = comment();
                else
                    throw new MethodNotAllowedException();
            }
            if (StringUtils.isMatched(path, "/board/show/\\d+")) {
                String[] tokens = path.split("/");
                Long postId = Long.parseLong(tokens[tokens.length - 1]);
                responseEntity = show(postId);
            }
            return responseEntity;
        } catch (MethodNotAllowedException e) {
            return MethodNotAllowedExceptionHandler.handle();
        } catch (FileNotFoundException e) {
            return FileNotFoundExceptionHandler.handle();
        }
    }

    private ResponseEntity<?> writeForm() throws IOException {
        boolean isLoggedIn = SessionManager.isLoggedIn(httpRequest);

        if (!isLoggedIn) {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create("/user/login.html"))
                    .build();
        }

        User loggedInUser = SessionManager.getLoggedInUser(httpRequest);

        String username = "<li><a role=\"button\">{{username}}</a></li>";
        username = username.replace("{{username}}", loggedInUser.getName());

        String html = new String(ResourceUtils.getStaticResource(httpRequest.getPath()));
        byte[] body = html.replace("<li id=\"username\"></li>", username)
                .getBytes();

        return ResponseEntity.ok()
                .contentType(MediaType.getContentType(httpRequest))
                .contentLength(body.length)
                .body(body);
    }

    private ResponseEntity<?> write() throws IOException {
        Map<String, String> query = httpRequest.getBodyParams();

        User writer = SessionManager.getLoggedInUser(httpRequest);
        String title = query.get("title");
        String contents = query.get("contents");

        try {
            boardService.write(writer, title, contents);

            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create("/index.html"))
                    .build();
        } catch (PostException e) {
            return PostExceptionHandler.handle(e);
        }
    }

    private ResponseEntity<?> show(Long postId) throws IOException {
        boolean isLoggedIn = SessionManager.isLoggedIn(httpRequest);

        if (!isLoggedIn) {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create("/user/login.html"))
                    .build();
        }

        User loggedInUser = SessionManager.getLoggedInUser(httpRequest);

        byte[] body = ShowTemplate.render(loggedInUser, boardService.getPostById(postId));

        return ResponseEntity.ok()
                .contentType(MediaType.getContentType(httpRequest))
                .contentLength(body.length)
                .body(body);
    }

    private ResponseEntity<?> comment() {
        Map<String, String> query = httpRequest.getBodyParams();

        User writer = SessionManager.getLoggedInUser(httpRequest);
        String postId = query.get("postId");
        String body = query.get("body");

        boardService.comment(Long.parseLong(postId), writer, body);

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create("/board/show/" + postId))
                .build();
    }
}
