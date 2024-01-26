package webserver;

import java.util.HashMap;
import java.util.Map;

public class Model{

    private Map<String, Object> modelAttributes = new HashMap<>();


    public Model addAttribute(String attributeName, Object attributeValue) {
        modelAttributes.put(attributeName, attributeValue);
        return this;
    }

    public Object getAttribute(String key){
        return modelAttributes.get(key);
    }
}
