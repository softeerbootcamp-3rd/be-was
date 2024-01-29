package exception;

import util.ResourceUtils;
import util.http.MediaType;
import util.http.ResponseEntity;

import java.io.IOException;

public class PostExceptionHandler {
    public static ResponseEntity<?> handle(PostException e) throws IOException {
        String line = "<div class=\"alert alert-danger\" role=\"alert\">{{message}}</div>";
        line = line.replace("{{message}}", e.getMessage());

        String html = new String(ResourceUtils.getStaticResource("/board/write.html"));
        byte[] body =  html.replace("<div id=\"replace\"></div>", line).getBytes();

        return ResponseEntity.badRequest()
                .contentType(MediaType.TEXT_HTML)
                .contentLength(body.length)
                .body(body);
    }
}
