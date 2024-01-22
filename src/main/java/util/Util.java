package util;

import java.util.HashMap;
import java.util.Map;

public class Util {

    private Util() {
        throw new AssertionError();
    }

    public static HashMap<String, String> parseQueryString(String queryString) {
        HashMap<String, String> hashMap = new HashMap<>();
        String[] keyAndValue = queryString.split("&");
        for(String keyValue : keyAndValue) {
            int indexOfEqual = keyValue.indexOf("=");
            String key = keyValue.substring(0, indexOfEqual);
            String value = keyValue.substring(indexOfEqual+1);
            hashMap.put(key, value);
        }
        return hashMap;
    }
}
