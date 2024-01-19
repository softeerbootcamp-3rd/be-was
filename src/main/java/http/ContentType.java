package http;

import java.util.HashMap;
import java.util.Map;

public enum ContentType {

    DEFAULT("application/octet-stream"),
    HTML("text/html"),
    CSS("text/css"),
    JS("application/x-javascript"),
    PNG("image/png"),
    ICO("image/x-icon"),
    PLAIN_TEXT("text/plain");

    private static final Map<String, ContentType> CONTENT_TYPE = new HashMap<>();

    static {
        CONTENT_TYPE.put("html", HTML);
        CONTENT_TYPE.put("css", CSS);
        CONTENT_TYPE.put("js", JS);
        CONTENT_TYPE.put("png", PNG);
        CONTENT_TYPE.put("ico", ICO);
        CONTENT_TYPE.put("txt", PLAIN_TEXT);
    }

    private final String type;

    ContentType(String type) {
        this.type = type;
    }

    public static ContentType getContentType(String extension) {
        return CONTENT_TYPE.getOrDefault(extension, DEFAULT);
    }

    public String getType() {
        return type;
    }
}
