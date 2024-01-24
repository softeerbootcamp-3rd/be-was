package httpmessage.request;

import java.util.HashMap;
import java.util.Map;

public class Parameter {
    private final Map<String, String> values;

    public Parameter(Map<String, String> values) {
        this.values = new HashMap<>(values);
    }

    public Map<String, String> getValues() {
        return values;
    }

    public String getValue(String key) {
        return values.get(key);
    }

}
