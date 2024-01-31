package util;

import exception.InvalidSignUpException;

import java.util.HashMap;
import java.util.Map;

public class QueryStringParser {

    public static Map<String, String> getParameters(String queryString) throws InvalidSignUpException {
        return parseQueryString(queryString);
    }
    private static Map<String, String> parseQueryString(String queryString) throws InvalidSignUpException {
        Map<String, String> keyValueMap = new HashMap<>();
        String[] parameters = queryString.split("&");
        for (String parameter : parameters) {
            String[] keyValue = parameter.split("=");
			if(keyValue.length != 2){
				throw new InvalidSignUpException("잘못된 입력입니다.");
			}
            String key = keyValue[0];
            String value = keyValue[1];
            keyValueMap.put(key, value);
        }
        return keyValueMap;
    }
}
