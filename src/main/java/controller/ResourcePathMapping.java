package controller;

import java.util.HashMap;
import java.util.Map;

public class ResourcePathMapping {

    private static Map<String, String> extensionToDirectoryPath = new HashMap<>();
    private static Map<String, String> extensionToContentType = new HashMap<>();

    static {
        extensionToDirectoryPath.put("html", "/templates");
        extensionToDirectoryPath.put("css", "/static");
        extensionToDirectoryPath.put("js", "/static");
        extensionToDirectoryPath.put("ico", "/static");
        extensionToDirectoryPath.put("png", "/static");
        extensionToDirectoryPath.put("jpg", "/static");
        extensionToDirectoryPath.put("woff", "/static");
        extensionToDirectoryPath.put("ttf", "/static");
    }

    static {
        extensionToContentType.put("html", "text/html");
        extensionToContentType.put("css", "text/css");
        extensionToContentType.put("js", "application/javascript");
        extensionToContentType.put("ico", "image/x-icon");
        extensionToContentType.put("png", "image/png");
        extensionToContentType.put("jpg", "image/jpg");
        extensionToContentType.put("woff", "font/woff");
        extensionToContentType.put("ttf", "font/ttf");
    }

    public static String getDirectory(String extension) {
        return extensionToDirectoryPath.get(extension);
    }

    public static String getContentType(String extension) {
        return extensionToContentType.get(extension);
    }
}
