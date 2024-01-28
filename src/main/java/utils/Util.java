package utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

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

    public static String readFile(StringBuilder stringBuilder, String path, HashMap<String, String> replace) {
        try (FileInputStream fileInputStream = new FileInputStream(path);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream, StandardCharsets.UTF_8))) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            for(String key : replace.keySet()) {
                int indexOfKey = stringBuilder.indexOf(key);
                if(indexOfKey == -1) continue;
                stringBuilder.replace(indexOfKey, indexOfKey + key.length(), replace.get(key));
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}