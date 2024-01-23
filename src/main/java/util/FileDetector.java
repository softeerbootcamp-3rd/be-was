package util;

import exception.BadRequestException;
import exception.NotFound;
import model.http.ContentType;

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

    public ContentType getContentType(String accept, String pathUrl) {
        try {
            return ContentType.getContentTypeByAccept(accept);
        } catch (BadRequestException e) {
            return ContentType.getContentTypeByExtension(parseFileExtension(pathUrl));
        }
    }

    private String parseFileExtension(String url) {
        String[] pathSegments = url.split("/");
        String lastSegment = pathSegments[pathSegments.length - 1];

        // 파일 확장자 추출, 없을시 "" return
        int lastDotIndex = lastSegment.lastIndexOf('.');
        return (lastDotIndex != -1) ? lastSegment.substring(lastDotIndex + 1) : "";
    }
    private Path getFilePath(String filePath) {
        if (filePath.equals("/")) {
            return new File(TEMPLATES_RESOURCE + "/index.html").toPath();
        }
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
