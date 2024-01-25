package util;

import webserver.http.HttpRequest;
import webserver.http.HttpStatus;
import webserver.http.HttpHeader;
import webserver.http.ResponseEntity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourceManager {

    private static final Map<String, String> CONTENT_TYPE_MAP = new HashMap<>();
    private static final String FILE_PATH = "src/main/resources";

    static {
        CONTENT_TYPE_MAP.put("html", "text/html");
        CONTENT_TYPE_MAP.put("css", "text/css");
        CONTENT_TYPE_MAP.put("js", "application/javascript");
        CONTENT_TYPE_MAP.put("png", "image/png");
        CONTENT_TYPE_MAP.put("jpg", "image/jpeg");
        CONTENT_TYPE_MAP.put("jpeg", "image/jpeg");
        CONTENT_TYPE_MAP.put("gif", "image/gif");
        CONTENT_TYPE_MAP.put("font", "image/svg+xml");
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

    public static ResponseEntity handleStaticResource(HttpRequest request) throws IOException {
        String path = request.getPath().equals("/") ? "/index.html" : request.getPath();
        String contentType = getContentType(path);

        StringBuilder pathBuilder = new StringBuilder(FILE_PATH);
        if (path.endsWith(".html")) {
            pathBuilder.append("/templates");
        } else {
            pathBuilder.append("/static");
        }
        pathBuilder.append(path);

        File file = new File(pathBuilder.toString());

        if (file.exists()) {
            byte[] body = readAllBytes(file);
            Map<String, List<String>> header = new HashMap<>();
            header.put(HttpHeader.CONTENT_TYPE, Collections.singletonList(contentType));
            return new ResponseEntity<>(HttpStatus.OK, header, body);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    private static byte[] readAllBytes(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file); ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            int size = (int) file.length();
            byte[] buffer = new byte[size];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }

            return bos.toByteArray();
        }
    }

}
