package webserver.http;

public enum HttpMethod {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    PATCH("PATCH"),
    DELETE("DELETE");

    private final String httpMethodType;

    HttpMethod(String httpMethodType) {
        this.httpMethodType = httpMethodType;
    }

    public String getHttpMethodType() {
        return httpMethodType;
    }
}
