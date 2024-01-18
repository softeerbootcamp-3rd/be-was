package util;

import model.http.ContentType;
import exception.NotFound;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileDetector {
    private static class FileDetectorHolder {
        private static final FileDetector INSTANCE = new FileDetector();
    }

    public static FileDetector getInstance() {
        return FileDetectorHolder.INSTANCE;
    }

    public static final String TEMPLATES_RESOURCE = "src/main/resources/templates";
    public static final String STATIC_RESOURCES = "src/main/resources/static";

    public ContentType getContentType(String contentType) {
        if (contentType.contains("css")) {
            return ContentType.CSS;
        }
        if (contentType.contains("js")) {
            return ContentType.JAVASCRIPT;
        }
        if (contentType.contains("html")) {
            return ContentType.HTML;
        }
        return ContentType.MIME;
    }

    public byte[] getNotFound() {
        try {
            return Files.readAllBytes(new File(TEMPLATES_RESOURCE + "/error/not_found.html").toPath());
        } catch (IOException e) {
            throw new NotFound("파일을 찾을 수 없습니다.");
        }
    }

    private Path getFilePath(String filePath) {
        if (filePath.contains("html")) {
            return new File(TEMPLATES_RESOURCE + filePath).toPath();
        }
        return new File(STATIC_RESOURCES + filePath).toPath();
    }

    public byte[] getFile(String filePath) {
        try {
            return Files.readAllBytes(getFilePath(filePath));
        } catch (IOException e) {
            throw new NotFound("파일을 찾을 수 없습니다.");
        }
    }
}