package constant;

public enum MimeType {
    HTML("html", "text/html;charset=utf-8"),
    CSS("css", "text/css"),
    JAVASCRIPT("js", "application/javascript"),
    PNG("png", "image/png"),
    JPG("jpg", "image/jpeg"),
    JPEG("jpeg", "image/jpeg"),
    GIF("gif", "image/gif"),
    ICON("ico", "image/x-icon"),
    EOT("eot", "application/vnd.ms-fontobject"),
    SVG("svg", "image/svg+xml"),
    TTF("ttf", "font/ttf"),
    WOFF("woff", "font/woff"),
    WOFF2("woff2", "woff2"),
    TEXT("txt", "text/plain"),
    OCTET_STREAM("", "application/octet-stream");

    private final String extension;
    private final String mimeType;


    MimeType(String extension, String mimeType) {
        this.extension = extension;
        this.mimeType = mimeType;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public static MimeType getByExtension(String extension) {
        for (MimeType type : values()) {
            if (type.extension.equals(extension)) {
                return type;
            }
        }
        return OCTET_STREAM;
    }
}
