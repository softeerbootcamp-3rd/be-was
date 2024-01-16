package webserver;

import java.util.Arrays;

public enum RequestHeader {

    HOST("Host"),
    CONNECTION("Connection"),
    ACCEPT("Accept"),
    NONE("");

    private final String name;

    RequestHeader(String name) {
        this.name = name;
    }

    public static RequestHeader findProperty(String input) {
        return Arrays.stream(values())
                .filter(property -> input.equals(property.name))
                .findFirst()
                .orElse(NONE);
    }
}
