package exception;

import model.User;
import util.ResourceUtils;
import util.http.MediaType;
import util.http.ResponseEntity;

import java.io.IOException;

public class PostExceptionHandler {
    public static final String USERNAME = "<li><a role=\"button\">{username}</a></li>";

    public static ResponseEntity<?> handle(User loggedInUser, PostException e) throws IOException {
        String alert = "<div class=\"alert alert-danger\" role=\"alert\">{message}</div>";
        alert = alert.replace("{message}", e.getMessage());

        String username = USERNAME.replace("{username}", loggedInUser.getName());

        String html = new String(ResourceUtils.getStaticResource("/board/write.html"));
        byte[] body =  html.replace("<div id=\"alert\"></div>", alert)
                .replace("<li id=\"username\"></li>", username)
                .getBytes();

        return ResponseEntity.badRequest()
                .contentType(MediaType.TEXT_HTML)
                .contentLength(body.length)
                .body(body);
    }
}
