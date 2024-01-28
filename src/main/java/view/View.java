package view;

import java.util.HashMap;
import java.util.Map;

public class View {

    private final Map<String, Object> attribute;

    public View() {
        attribute = new HashMap<>();
    }

    public Object get(String objectName) {
        return attribute.get(objectName);
    }
}
