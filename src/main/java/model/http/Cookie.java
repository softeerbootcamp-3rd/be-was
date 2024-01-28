package model.http;

import java.util.HashMap;
import java.util.Map;

public class Cookie {
    private final String key;
    private final String value;
    private final HashMap<String, String> option;
    public Cookie(String key, String value, HashMap<String, String> parameter) {
        this.key = key;
        this.value = value;
        this.option = parameter;
    }
    public String getCookieList() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(key).append("=").append(value).append("; ");
        for (Map.Entry<String, String> entry : option.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            stringBuilder.append(key).append("=").append(value).append("; ");
        }
        return stringBuilder.toString();
    }
}
