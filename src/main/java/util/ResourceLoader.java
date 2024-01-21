package util;

import constant.HttpStatus;
import webserver.HttpRequest;
import webserver.HttpResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ResourceLoader {
    private static final Map<String, String> CONTENT_TYPE_MAP = new HashMap<>();

    static {
        CONTENT_TYPE_MAP.put("html", "text/html;charset=utf-8");
        CONTENT_TYPE_MAP.put("css", "text/css");
        CONTENT_TYPE_MAP.put("js", "application/javascript");
        CONTENT_TYPE_MAP.put("png", "image/png");
        CONTENT_TYPE_MAP.put("jpg", "image/jpeg");
        CONTENT_TYPE_MAP.put("jpeg", "image/jpeg");
        CONTENT_TYPE_MAP.put("gif", "image/gif");
        CONTENT_TYPE_MAP.put("ico", "image/x-icon");
        CONTENT_TYPE_MAP.put("eot", "application/vnd.ms-fontobject");
        CONTENT_TYPE_MAP.put("svg", "image/svg+xml");
        CONTENT_TYPE_MAP.put("ttf", "font/ttf");
        CONTENT_TYPE_MAP.put("woff", "font/woff");
        CONTENT_TYPE_MAP.put("woff2", "woff2");
    }

    public static String getContentType(String path) {
        String extension = extractFileExtension(path);
        return CONTENT_TYPE_MAP.getOrDefault(extension, "application/octet-stream");
    }

    private static String extractFileExtension(String filePath) {
        if (filePath == null || filePath.isEmpty()) return "";

        int lastDotIndex = filePath.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == filePath.length() - 1) return "";

        return filePath.substring(lastDotIndex + 1);
    }

    public static HttpResponse getFileResponse(HttpRequest request) throws IOException {
        String basePath = "src/main/resources/templates";
        if (request.getPath().startsWith("/css/") || request.getPath().startsWith("/fonts/")
                || request.getPath().startsWith("/images/") || request.getPath().startsWith("/js/"))
            basePath = "src/main/resources/static";

        Path filePath = Paths.get(basePath + request.getPath());
        if (Files.exists(filePath) && Files.isRegularFile(filePath)) {
            byte[] content = Files.readAllBytes(filePath);
            return HttpResponse.builder()
                    .status(HttpStatus.OK)
                    .addHeader("Content-Type", getContentType(request.getPath()))
                    .body(content)
                    .build();
        } else {
            return HttpResponse.builder()
                    .status(HttpStatus.NOT_FOUND)
                    .addHeader("Content-Type", "text/plain")
                    .body(HttpStatus.NOT_FOUND.getFullMessage())
                    .build();
        }
    }
}
