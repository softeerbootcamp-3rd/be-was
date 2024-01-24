package http;

public enum MimeType {
    HTML("text/html"),
    CSS("text/css"),
    JS("text/javascript"),
    WOFF("application/x-font"),
    TTF("application/x-font"),
    ICO("image/x-icon"),
    JPEG("image/jpeg"),
    PNG("image/png");

    private final String value;

    MimeType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}