package model;

import com.google.common.collect.Maps;

import java.util.Map;

public class Model {
    public static Map<String, Object> modelMap = Maps.newConcurrentMap();

    public static void addAttribute(String name, Object object) {
        modelMap.put(name, object);
    }

    public static Object getAttribute(String name) {
        return modelMap.get(name);
    }
}
