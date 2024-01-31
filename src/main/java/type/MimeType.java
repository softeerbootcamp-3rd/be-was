package type;

public enum MimeType {

    TEXT("txt", "text/plain"),
    HTML("html", "text/html;charset=utf-8"),
    CSS("css", "text/css"),
    JAVASCRIPT("js", "application/javascript"),
    JSON("", "application/json"),
    PNG("png", "image/png"),
    JPG("jpg", "image/jpeg"),
    JPEG("jpeg", "image/jpeg"),
    GIF("gif", "image/gif"),
    SVG("svg", "image/svg+xml"),
    WOFF("woff", "font/woff"),
    TTF("ttf", "font/ttf"),
    DEFAULT("", "application/octet-stream");

    private String extension;
    private String contentType;


    MimeType(String extension, String contentType) {
        this.extension = extension;
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }

    public static MimeType getMimeTypeByExtension(String extension) {
        for (MimeType mimeType : MimeType.values()) {
            if (mimeType.extension.equalsIgnoreCase(extension)) {
                return mimeType;
            }
        }
        return DEFAULT;
    }

}
