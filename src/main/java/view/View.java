package view;

import java.util.HashMap;
import java.util.Map;

public class View {

    private final Map<String, Object> attribute;

    public View() {
        attribute = new HashMap<>();
    }

    public <T> T get(String key, Class<T> type) {
        Object value = attribute.get(key);

        if (value != null && type.isInstance(value)) {
            return type.cast(value);
        }
        return null;
    }

    public void set(String key, Object value) {
        attribute.put(key, value);
    }
}
