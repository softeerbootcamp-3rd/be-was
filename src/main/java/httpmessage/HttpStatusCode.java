package httpmessage;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum HttpStatusCode {
    OK(200, "OK"),
    FOUND(302, "Found"),
    NOT_FOUND(404, "Not Found"),
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
