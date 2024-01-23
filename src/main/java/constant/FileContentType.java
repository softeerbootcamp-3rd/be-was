package constant;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum FileContentType {
    TXT (".txt", "text/plain"),
    HTML (".html", "text/html"),
    CSS (".css", "text/css"),
    JS (".js", "text/javascript"),

    PNG (".png", "image/png"),
    SVG (".svg", "image/svg+xml"),
    ICO (".ico", "image/vnd.microsoft.icon"),

    XML (".xml", "application/xml"),
    EOT (".eot", "application/vnd.ms-fontobject"),

    TTF (".ttf", "font/ttf"),
    WOFF (".woff", "font/woff"),
    WOFF2 (".woff2", "font/woff2");

    private final String extension;
    private final String contentType;

    public String getExtension() {
        return extension;
    }

    public String getContentType() {
        return contentType;
    }

    private static final Map<String, FileContentType> fileContentTypeMap = Collections.unmodifiableMap(
            Stream.of(values()).collect(Collectors.toMap(FileContentType::getExtension, Function.identity()))
    );

    FileContentType(String extension, String contentType) {
        this.extension = extension;
        this.contentType = contentType;
    }

    public static FileContentType of(String extension) {
        return fileContentTypeMap.get(extension);
    }
}
