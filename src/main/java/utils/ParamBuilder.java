package utils;

import java.util.HashMap;
import java.util.Map;

public class ParamBuilder {

    public static Map<String, String> getParams(String url) {
        Map<String, String> params = new HashMap<>();

        String[] splitQuery = url.split("\\?")[1].split("&");
        for (String query : splitQuery) {
            String[] split = query.split("=", -1);
            params.put(split[0], split[1]);
        }

        return params;
    }
}
