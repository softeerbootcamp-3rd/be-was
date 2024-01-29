package exception;

import util.ResourceUtils;
import util.http.HttpStatus;
import util.http.MediaType;
import util.http.ResponseEntity;

import java.io.IOException;

public class MethodNotAllowedExceptionHandler {
    public static ResponseEntity<?> handle() throws IOException {
        String html = new String(ResourceUtils.getStaticResource("/error.html"));
        byte[] body = html.replace("<h1 id=\"code\"></h1>", "<h1 id=\"code\">405</h1>")
                .replace("<h2 id=\"phrase\"></h2>","<h2 id=\"phrase\">Method Not Allowed</h2>")
                .getBytes();

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .contentType(MediaType.TEXT_HTML)
                .contentLength(body.length)
                .body(body);
    }
}
