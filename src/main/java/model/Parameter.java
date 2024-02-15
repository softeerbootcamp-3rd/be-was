package model;

import com.google.common.collect.Maps;

import java.util.Map;

public class Parameter {
    private Map<String, String> paramMap = Maps.newHashMap();

    public void put(String key, String value) {
        paramMap.put(key, value);
    }

    public String get(String key) {
        return paramMap.get(key);
    }

    public Map<String, String> getParamMap() {
        return paramMap;
    }
}
