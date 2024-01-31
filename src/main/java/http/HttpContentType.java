package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.HashMap;
import java.util.Map;

public class HttpContentType {
    private static final Logger logger = LoggerFactory.getLogger(HttpContentType.class);
    private static final Map<String,String> httpContentType = new HashMap<>();

    static {
        httpContentType.put("html","text/html");
        httpContentType.put("css","text/css");
        httpContentType.put("js","text/javascript");
        httpContentType.put("ico","image/x-icon");
        httpContentType.put("png","image/png");
        httpContentType.put("jpg","image/jpeg");
        httpContentType.put("woff","application/font-woff");
    }

    public static String getValue(String key){
        return httpContentType.get(key);
    }
}
