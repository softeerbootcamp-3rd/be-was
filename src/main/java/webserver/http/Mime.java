package webserver.http;

public enum Mime {
    TEXT_HTML("text/html", "html"),
    TEXT_CSS("text/css", "css"),
    TEXT_JAVASCRIPT("text/javascript", "js"),
    IMAGE_JPG("image/jpeg", "jpg"),
    IMAGE_PNG("image/png", "png"),
    IMAGE_ICO("image/ico", "ico"),
    APPLICATION_JSON("application/json", "json"),
    FONT_TTF("fonts/ttf", "ttf"),
    FONT_WOFF("fonts/woff", "woff"),
    NONE("none", "");

    private final String mimeType;
    private final String extension;

    Mime(String mimeType, String extension) {
        this.mimeType = mimeType;
        this.extension = extension;
    }

    public String getMimeType() {
        return mimeType;
    }

    public static Mime convertMime(String ext) {
        for (Mime m : Mime.values()) {
            if (m.extension.equalsIgnoreCase(ext)) {
                return m;
            }
        }
        return Mime.NONE;
    }
}
