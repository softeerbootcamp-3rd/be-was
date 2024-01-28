package exception;

import util.ResourceUtils;
import util.http.HttpHeaders;
import util.http.HttpStatus;
import util.http.MediaType;
import util.http.ResponseEntity;

import java.io.IOException;

public class FileNotFoundExceptionHandler {
    public static ResponseEntity<?> handle() throws IOException {
        byte[] body = ResourceUtils.getStaticResource("/notfound.html");

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.TEXT_HTML)
                .contentLength(body.length)
                .body(body);
    }
}
