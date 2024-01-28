package dto.request;

import java.util.Map;

public class FirstClassCollection {

    private Map<String, String> map;

    public FirstClassCollection(Map<String, String> map) {
        this.map = map;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public String getValue(String key) {
        return map.get(key);
    }
}
