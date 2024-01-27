package exception;

import util.ResourceUtils;
import util.http.HttpHeaders;
import util.http.HttpStatus;
import util.http.MediaType;
import util.http.ResponseEntity;

import java.io.IOException;


public class CreateUserExceptionHandler {
    public static ResponseEntity<?> handle(CreateUserException e) throws IOException {
        String line = "<div class=\"alert alert-danger\" role=\"alert\">{{message}}</div>";
        line = line.replace("{{message}}", e.getMessage());

        String html = new String(ResourceUtils.getStaticResource("/user/form.html"));
        byte[] body =  html.replace("<div id=\"replace\"></div>", line).getBytes();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE)
                .header(HttpHeaders.CONTENT_LENGTH, Integer.toString(body.length))
                .body(body);
    }
}
