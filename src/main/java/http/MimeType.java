package http;

public enum MimeType {
    HTML("html", "text/html"),
    CSS("css", "text/css"),
    JS("js", "application/javascript"),
    PNG("png", "image/png"),
    JPG("jpg", "image/jpeg"),
    JPEG("jpeg", "image/jpeg"),
    SVG("svg", "image/svg+xml"),
    EOT("eot", "application/vnd.ms-fontobject"),
    TTF("ttf", "font/ttf"),
    ICO("ico", "image/x-icon"),
    WOFF("woff", "font/woff"),
    WOFF2("woff2", "font/woff2");

    private final String extension;
    private final String contentType;

    MimeType(String extension, String contentType) {
        this.extension = extension;
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }

    public static String getContentTypeByExtension(String filePath) {
        String extension = filePath.substring(filePath.lastIndexOf(".") + 1);
        for (MimeType mimeType : MimeType.values()) {
            if (mimeType.extension.equals(extension)) {
                return mimeType.getContentType();
            }
        }
        return "text/html";
    }
}
