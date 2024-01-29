package constant;

public enum HttpHeader {
    // 공통 요청 헤더
    HOST("Host"),
    USER_AGENT("User-Agent"),
    ACCEPT("Accept"),
    ACCEPT_LANGUAGE("Accept-Language"),

    // 공통 응답 헤더
    DATE("Date"),
    SERVER("Server"),
    CONTENT_TYPE("Content-Type"),
    CONTENT_LENGTH("Content-Length"),
    CONTENT_DISPOSITION("Content-Disposition"),

    // 요청 헤더
    AUTHORIZATION("Authorization"),
    REFERER("Referer"),
    COOKIE("Cookie"),

    // 응답 헤더
    SET_COOKIE("Set-Cookie"),
    LOCATION("Location"),
    CACHE_CONTROL("Cache-Control"),
    EXPIRES("Expires"),

    // 업그레이드 및 연결 헤더
    UPGRADE("Upgrade"),
    CONNECTION("Connection"),

    // 보안 헤더
    STRICT_TRANSPORT_SECURITY("Strict-Transport-Security"),
    CONTENT_SECURITY_POLICY("Content-Security-Policy"),
    X_CONTENT_TYPE_OPTIONS("X-Content-Type-Options");

    private final String value;

    HttpHeader(String value) {
        this.value = value;
    }

    public static HttpHeader of(String value) {
        for (HttpHeader header : values()) {
            if (header.value.equalsIgnoreCase(value)) {
                return header;
            }
        }
        throw new IllegalArgumentException("Unsupported header: " + value);
    }

    public String getValue() {
        return this.value;
    }
}
