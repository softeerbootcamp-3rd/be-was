package util;

import java.util.Arrays;
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

    public static HashMap<String, String> parseStringJson(String json) {
        HashMap<String, String> hashMap = new HashMap<>();
        json = json.substring(1, json.length()-1).trim();
        String[] jsonArray = json.split(",");
        for(String keyAndValue : jsonArray) {
            String[] tokens = keyAndValue.split(":");
            String key = tokens[0].trim().replace("\"", "");
            String value = tokens[1].trim().replace("\"", "");
            hashMap.put(key, value);
        }
        return hashMap;
    }

    public static HashMap<String, String> parseSemicolon(String semicolon) {
        HashMap<String, String> hashMap = new HashMap<>();
        String[] tokens = semicolon.split("; ");
        for(String token : tokens) {
            int indexOfEqual = token.indexOf("=");
            if(indexOfEqual == -1) continue;
            String key = token.substring(0, indexOfEqual);
            String value = token.substring(indexOfEqual+1);
            hashMap.put(key, value);
        }
        return hashMap;
    }
}