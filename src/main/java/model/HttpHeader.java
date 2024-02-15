package model;

import com.google.common.collect.Maps;

import java.util.Map;

public class HttpHeader {
    private Map<String, String> header = Maps.newHashMap();

    public void put(String key, String value) {
        header.put(key, value);
    }

    public String get(String key) {
        return header.get(key);
    }

    public Map<String, String> getHeader() {
        return header;
    }
}
