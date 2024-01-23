package webserver.http.response.enums;

public enum ContentType {
    HTML("html", "text/html"),
    CSS("css", "text/css"),
    JS("js", "application/javascript"),
    ICO("ico", "image/x-icon"),
    PNG("png", "image/png"),
    JPG("jpg", "image/jpeg"),
    WOFF("woff", "font/woff"),
    TTF("ttf", "font/ttf");


    private final String extension;
    private final String mimeType;

    ContentType(String extension, String mimeType) {
        this.extension = extension;
        this.mimeType = mimeType;
    }

    public static ContentType toContentType(String extension) {
        for (ContentType type : values()) {
            if (type.extension.equals(extension)) {
                return type;
            }
        }
        return HTML;
    }

    public String getMimeType(){
        return mimeType;
    }
}
