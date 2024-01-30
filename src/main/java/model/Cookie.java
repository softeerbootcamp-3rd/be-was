package model;

import com.google.common.collect.Maps;

import java.util.Map;

public class Cookie {
    private Map<String, String> cookieMap = Maps.newHashMap();

    public void put(String key, String value) {
        cookieMap.put(key, value);
    }

    public String get(String key) {
        return cookieMap.get(key);
    }

    public Map<String, String> getCookieMap() {
        return cookieMap;
    }
}
