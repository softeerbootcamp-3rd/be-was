package util;

import util.http.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ResourceUtils {
    private static final String TEMPLATES = "./src/main/resources/templates";
    private static final String STATIC = "./src/main/resources/static";

    public static ResponseEntity<?> getStaticResource(HttpRequest httpRequest) {
        try {
            String path = httpRequest.getPath();

            byte[] body = Files.readAllBytes(getFilePath(path));
            String contentType = httpRequest.getHeader(HttpHeaders.ACCEPT);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, contentType)
                    .header(HttpHeaders.CONTENT_LENGTH, Integer.toString(body.length))
                    .body(body);
        } catch (IOException e) {
            byte[] body = "page not found".getBytes();
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(body);
        }
    }

    private static Path getFilePath(String path) {
        if (path.equals("/"))
            path = "/index.html";

        String extension = getExtension(path);

        File file;
        if (extension.equals("html")) {
            file = new File(TEMPLATES + path);
            if (!file.exists())
                file = new File(STATIC + path);
        } else
            file = new File(STATIC + path);

        return file.toPath();
    }

    private static String getExtension(String path) {
        String[] tokens = path.split("\\.");
        return tokens[tokens.length - 1];
    }
}
