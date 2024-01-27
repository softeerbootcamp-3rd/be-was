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
                .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE)
                .header(HttpHeaders.CONTENT_LENGTH, Integer.toString(body.length))
                .body(body);
    }
}
