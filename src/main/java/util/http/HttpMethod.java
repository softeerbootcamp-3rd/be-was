package util.http;

import util.Assert;

public final class HttpMethod {
    public static final HttpMethod GET = new HttpMethod("GET");
    public static final HttpMethod HEAD = new HttpMethod("HEAD");
    public static final HttpMethod POST = new HttpMethod("POST");
    public static final HttpMethod PUT = new HttpMethod("PUT");
    public static final HttpMethod PATCH = new HttpMethod("PATCH");
    public static final HttpMethod DELETE = new HttpMethod("DELETE");
    public static final HttpMethod OPTIONS = new HttpMethod("OPTIONS");
    public static final HttpMethod TRACE = new HttpMethod("TRACE");
    private static final HttpMethod[] values = new HttpMethod[] { GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE };

    private final String name;


    private HttpMethod(String name) {
        this.name = name;
    }

    public static HttpMethod[] values() {
        HttpMethod[] copy = new HttpMethod[values.length];
        System.arraycopy(values, 0, copy, 0, values.length);
        return copy;
    }

    public static HttpMethod valueOf(String method) {
        Assert.notNull(method, "Method must not be null");
        switch (method) {
            case "GET":
                return GET;
            case "HEAD":
                return HEAD;
            case "POST":
                return POST;
            case "PUT":
                return PUT;
            case "PATCH":
                return PATCH;
            case "DELETE":
                return DELETE;
            case "OPTIONS":
                return OPTIONS;
            case "TRACE":
                return TRACE;
            default:
                return new HttpMethod(method);
        }
    }

    @Override
    public String toString() {
        return this.name;
    }

}

