package util;

import constant.HttpStatus;
import constant.MimeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.HttpRequest;
import webserver.HttpResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ResourceLoader {

    private static final Logger logger = LoggerFactory.getLogger(ResourceLoader.class);

    public static String getMimeType(String path) {
        String extension = extractFileExtension(path);
        return MimeType.getByExtension(extension).getMimeType();
    }

    private static String extractFileExtension(String filePath) {
        if (filePath == null || filePath.isEmpty()) return "";

        int lastDotIndex = filePath.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == filePath.length() - 1) return "";

        return filePath.substring(lastDotIndex + 1);
    }

    public static HttpResponse getFileResponse(String path) throws IOException {
        String basePath = "src/main/resources/templates";
        if (path.startsWith("/css/") || path.startsWith("/fonts/")
                || path.startsWith("/images/") || path.startsWith("/js/"))
            basePath = "src/main/resources/static";

        File file = new File(basePath + path);
        if (file.exists() && file.isFile()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                byte[] content = new byte[(int) file.length()];
                fis.read(content);
                return HttpResponse.builder()
                        .status(HttpStatus.OK)
                        .addHeader("Content-Type", getMimeType(path))
                        .body(content)
                        .build();
            } catch (IOException e) {
                logger.error(e.getMessage());
                return HttpResponse.builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(HttpStatus.INTERNAL_SERVER_ERROR.getFullMessage())
                        .build();
            }
        } else {
            return HttpResponse.builder()
                    .status(HttpStatus.NOT_FOUND)
                    .addHeader("Content-Type", "text/plain")
                    .body(HttpStatus.NOT_FOUND.getFullMessage())
                    .build();
        }
    }
}
