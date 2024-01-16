package model;

import java.util.HashMap;
import java.util.Map;

public class MimeType {
    private static final Map<String, String> mimeTypes = new HashMap<>();

    static {
        mimeTypes.put("html", "text/html");
        mimeTypes.put("css", "text/css");
        mimeTypes.put("js", "application/javascript");
        mimeTypes.put("png", "image/png");
        mimeTypes.put("jpg", "image/jpeg");
        mimeTypes.put("jpeg", "image/jpeg");
    }

    public static String getContentType(String extension) {
        return mimeTypes.getOrDefault(extension, "text/html");
    }
}
