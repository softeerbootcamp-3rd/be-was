package session;

import java.util.*;

public class Session {
    private Map<String, Object> attributes = new HashMap<>();

    public void setAttribute(String attributeName, Object attributeValue) {
        attributes.put(attributeName, attributeValue);
    }

    public Object getAttribute(String attributeName) {
        return attributes.get(attributeName);
    }
}
