package util;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum MimeType {
    HTML(".html", "text/html"),
    CSS(".css", "text/css"),
    JS(".js", "text/javascript"),
    PNG(".png", "image/png"),
    ICO(".ico", "image/x-icon"),
    SVG(".svg", "image/svg+xml"),
    WOFF(".woff", "font/woff"),
    TTF(".ttf", "font/ttf"),
    EOT(".eot", "font/eot"),
    WOFF2(".woff2", "font/woff2");


    private final String extension;
    private final String contentType;

    private static final Map<String, String> mimeTypeBundle = Collections.unmodifiableMap(
            Stream.of(values()).collect(Collectors.toMap(MimeType::getExtension, MimeType::getContentType))
    );

    MimeType(String extension, String contentType) {
        this.extension = extension;
        this.contentType = contentType;
    }

    public String getExtension() {
        return this.extension;
    }

    public String getContentType() {
        return this.contentType;
    }

    public static String getMimeType (String extension) {
        return mimeTypeBundle.get(extension);
    }
}
