package exception;

import model.User;
import util.ResourceUtils;
import util.SessionManager;
import util.http.*;

import java.io.IOException;


public class CreateUserExceptionHandler {
    public static ResponseEntity<?> handle(CreateUserException e) throws IOException {
        String line = "<div class=\"alert alert-danger\" role=\"alert\">{{message}}</div>";
        line = line.replace("{{message}}", e.getMessage());

        String html = new String(ResourceUtils.getStaticResource("/user/form.html"));
        byte[] body =  html.replace("<div id=\"replace\"></div>", line).getBytes();

        return ResponseEntity.badRequest()
                .contentType(MediaType.TEXT_HTML)
                .contentLength(body.length)
                .body(body);
    }

    public static ResponseEntity<?> handle(CreateUserException e, HttpRequest httpRequest) throws IOException {
        User loggedInUser = SessionManager.getLoggedInUser(httpRequest);

        String input = "<input class=\"form-control\" id=\"userId\" name=\"userId\" placeholder=\"{{userId}}\" disabled>";
        input = input.replace("{{userId}}", loggedInUser.getUserId());

        String li = "<li><a role=\"button\">{{userId}}</a></li>";
        li = li.replace("{{userId}}", loggedInUser.getUserId());

        String line = "<div class=\"alert alert-danger\" role=\"alert\">{{message}}</div>";
        line = line.replace("{{message}}", e.getMessage());

        String html = new String(ResourceUtils.getStaticResource("/user/update.html"));
        byte[] body =  html.replace("<div id=\"replace\"></div>", line)
                .replace("<input class=\"form-control\" id=\"userId\" name=\"userId\" placeholder=\"User ID\">", input)
                .replace("<li><a href=\"../user/login.html\" role=\"button\">로그인</a></li>", li)
                .getBytes();

        return ResponseEntity.badRequest()
                .contentType(MediaType.TEXT_HTML)
                .contentLength(body.length)
                .body(body);
    }
}
