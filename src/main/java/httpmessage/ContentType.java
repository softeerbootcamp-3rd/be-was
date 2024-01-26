package httpmessage;

public enum ContentType {
    HTML("html", "text/html;charset=utf-8"),
    CSS("css", "text/css;charset=utf-8"),
    JSON("json", "application/json"),
    JS("js", "application/javascript;charset=utf-8"),
    PNG("png", "image/png"),
    JPG("jpg", "image/jpg"),
    ICO("ico", "image/x-icon"),
    EOT("eot", "font/eot"),
    SVG("svg", "font/svg"),
    TTF("ttf", "font/ttf"),
    WOFF("woff", "font/woff"),
    WOFF2("woff2", "font/woff2");

    private final String fileExtension;
    private final String contentType;

    ContentType(String fileExtension, String contentType) {
        this.fileExtension = fileExtension;
        this.contentType = contentType;
    }

    public static String getContentType(String fileName) {
        String extension = getFileExtension(fileName);
        for (ContentType mediaType : values()) {
            if (mediaType.fileExtension.equals(extension)) {
                return mediaType.contentType;
            }
        }
        return "text/html;charset=utf-8";
    }

    private static String getFileExtension(String path) {
        int dotIndex = path.lastIndexOf(".");
        return (dotIndex == -1) ? "" : path.substring(dotIndex + 1);
    }
}
