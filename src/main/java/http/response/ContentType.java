package http.response;

public enum ContentType {
    HTML("text/html;charset=utf-8"),
    CSS("text/css;charset=utf-8"),
    JAVASCRIPT("application/javascript;charset=utf-8"),
    PNG("image/png"),
    EOT("application/vnd.ms-fontobject"),
    TTF("application/font-sfnt"),
    SVG("image/svg+xml"),
    WOFF("font/woff"),
    WOFF2("font/woff2"),
    JSON("application/json;charset=utf-8");

    private final String value;

    ContentType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ContentType getByFileExtension(String fileExtension) {
        for (ContentType type : values()) {
            if (type.name().equalsIgnoreCase(fileExtension)) {
                return type;
            }
        }
        return JSON; // 기본값으로 JSON을 반환
    }
}
