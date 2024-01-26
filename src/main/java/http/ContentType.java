package http;

import java.util.Arrays;

public enum ContentType {

    HTML("html", "text/html"),
    CSS("css", "text/css"),
    JS("js", "text/javascript"),
    WOFF("woff", "font/woff"),
    ICON("ico", "image/vnd.microsoft.icon"),
    NONE("", "");

    private final String extension;
    private final String mimeType;

    ContentType(String extension, String mimeType) {
        this.extension = extension;
        this.mimeType = mimeType;
    }

    public String getExtension() {
        return extension;
    }

    public static String getMimeType(String extension) {
        return Arrays.stream(values())
                .filter(value -> value.getExtension().equals(extension))
                .findFirst()
                .orElse(NONE)
                .mimeType;
    }
}
