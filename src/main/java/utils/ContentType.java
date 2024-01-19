package utils;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ContentType {

    HTML("html", "text/html"),
    CSS("css", "text/css"),
    JS("js", "text/javascript"),
    ICO("ico", "image/x-icon"),
    TTF("ttf", "font/ttf"),
    WOFF("woff", "font/woff"),
    WOFF2("woff2", "font/woff2"),
    SVG("svg", "image/svg+xml"),
    PNG("png", "image/png"),
    EOT("eot", "application/vnd.ms-fontobject");

    private final String extension;
    private final String contentType;

    private static final Map<String, String> contentTypes = Collections.unmodifiableMap(
            Stream.of(values())
                    .collect(Collectors.toMap(ContentType::getExtension, ContentType::name)));

    ContentType(String extension, String contentType) {
        this.extension = extension;
        this.contentType = contentType;
    }

    public String getExtension() {
        return this.extension;
    }

    public String getContentType() {
        return this.contentType;
    }

    public static String findContentType(String url) throws NullPointerException {
        String[] splitUrl = url.split("\\.");
        String extension = splitUrl[splitUrl.length - 1];
        try {
            String type = contentTypes.get(extension);
            ContentType contentType = ContentType.valueOf(type);
            return contentType.getContentType();
        } catch (NullPointerException e) {
            throw new NullPointerException();
        }
    }
}
