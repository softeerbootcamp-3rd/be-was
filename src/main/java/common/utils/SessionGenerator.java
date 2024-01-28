package common.utils;

import java.util.UUID;

public class SessionGenerator {
    public static String generateSessionId() {
        return UUID.randomUUID().toString();
    }
}
