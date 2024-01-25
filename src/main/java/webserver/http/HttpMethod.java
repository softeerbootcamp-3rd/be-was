package webserver.http;

public enum HttpMethod {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    PATCH("PATCH"),
    DELETE("DELETE"),
    NULL("NULL");

    private final String httpMethodType;

    HttpMethod(String httpMethodType) {
        this.httpMethodType = httpMethodType;
    }

    public String getHttpMethodType() {
        return httpMethodType;
    }

    public static HttpMethod converHttpMethodType(String ext) {
        for (HttpMethod httpMethodType : HttpMethod.values()) {
            if (httpMethodType.getHttpMethodType().equals(ext)) {
                return httpMethodType;
            }
        }
        return HttpMethod.NULL;
    }
}
