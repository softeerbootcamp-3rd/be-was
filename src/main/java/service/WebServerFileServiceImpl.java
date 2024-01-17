package service;

import model.http.request.HttpRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class WebServerFileServiceImpl implements WebServerFileService {
    public static final String TEMPLATES_RESOURCE = "src/main/resources/templates";
    public static final String STATIC_RESOURCES = "src/main/resources/static";
    public Path getFilePath(HttpRequest header) {
        String filePath = header.getStartLine().getPathUrl();
        if (filePath.contains("html")) {
            return new File(TEMPLATES_RESOURCE + filePath).toPath();
        }
        return new File(STATIC_RESOURCES + filePath).toPath();
    }

    @Override
    public byte[] getFile(HttpRequest httpRequest) {
        try {
            return Files.readAllBytes(getFilePath(httpRequest));
        } catch (IOException e) {
            return new byte[0];
        }
    }
}