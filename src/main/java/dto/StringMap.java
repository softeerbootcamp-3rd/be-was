package dto;

import java.util.Map;

public class StringMap {
    private Map<String, String> map;

    public StringMap(Map<String, String> map) {
        this.map = map;
    }
    public Map<String, String> getMap(){
        return map;
    }
    public String getValue(String key) {
        return map.get(key);
    }
}
