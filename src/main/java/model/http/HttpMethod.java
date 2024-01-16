package model.http;

public enum HttpMethod {
    GET("GET"), POST("POST"), PATCH("PATCH"), PUT("PUT"), OPTIONS("OPTIONS"), DELETE("DELETE");

    public String getMethod() {
        return method;
    }

    HttpMethod(String method) {
        this.method = method;
    }

    private final String method;
}
