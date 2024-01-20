package utils;

import java.util.HashMap;
import java.util.Map;

public class ParamBuilder {

    /**
     * 요청 url에서 쿼리의 키와 값을 분리해 반환합니다.
     *
     * @param url 요청 url
     * @return 분리한 쿼리의 키와 값 (Map)
     */
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
