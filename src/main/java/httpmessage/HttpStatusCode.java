package httpmessage;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum HttpStatusCode {


    // 1xx Informational
    CONTINUE(100, "Continue"),
    // 2xx Success
    OK(200, "OK"),
    // 3xx Redirection
    MOVED_TEMPORARILY(302, "Moved Temporarily"),
    // --- 4xx Client Error ---
    NOT_FOUND(404, "Not Found"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    // --- 5xx Server Error ---
    INTERNAL_SERVER_ERROR(500, "Internal Server Error");


    private static final Map<Integer, String> values = new HashMap<>();

    static {
        Arrays.stream(values())
                .forEach(it -> values.put(it.value, it.reasonPhrase));
    }

    private final int value;
    private final String reasonPhrase;

    HttpStatusCode(int value, String reasonPhrase) {
        this.value = value;
        this.reasonPhrase = reasonPhrase;
    }

    public static String findBy(int statusCode) {
        return values.get(statusCode);
    }

    public int getValue() {
        return value;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }

}
