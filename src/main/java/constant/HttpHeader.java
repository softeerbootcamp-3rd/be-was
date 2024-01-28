package constant;

public enum HttpHeader {
    CONTENT_TYPE("Content-Type"),
    CONTENT_LENGTH("Content-Length"),
    COOKIE("Cookie"),
    ACCEPT("Accept"),
    ACCEPT_LANGUAGE("Accept-Language"),
    ACCEPT_ENCODING("Accept-Encoding"),
    CONNECTION("Connection"),
    USER_AGENT("User-Agent"),
    REFERER("Referer"),
    HOST("Host"),
    ORIGIN("Origin"),
    SEC_FETCH_SITE("Sec-Fetch-Site"),
    SEC_FETCH_DEST("Sec-Fetch-Dest"),
    SEC_FETCH_MODE("Sec-Fetch-Mode"),
    SET_COOKIE("Set-Cookie"),
    LOCATION("Location");

    private final String headerName;

    HttpHeader(String headerName) {
        this.headerName = headerName;
    }

    public static HttpHeader fromHeaderName(String headerName) {
        for (HttpHeader header : values()) {
            if (header.headerName.equalsIgnoreCase(headerName)) {
                return header;
            }
        }
        throw new IllegalArgumentException("Unsupported header: " + headerName);
    }

    public String getHeaderName() {
        return headerName;
    }
}
