package utils;

import java.util.HashMap;
import java.util.Map;

public class ParamBuilder {

    /**
     * 요청 url에서 쿼리의 키와 값을 분리해 반환합니다.
     *
     * @param url 요청 url
     * @return 파라미터 (Map)
     */
    public static Map<String, String> getParamFromUrl(String url) {
        String[] splitQuery = url.split("\\?")[1].split("&");
        return getParamMap(splitQuery);
    }

    /**
     * 요청 메시지의 본문에서 쿼리의 키와 값을 분리해 반환합니다.
     *
     * @param body 요청 메시지 본문
     * @return 파라미터 (Map)
     */
    public static Map<String, String> getParamFromBody(String body) {
        String[] splitQuery = body.split("&");
        return getParamMap(splitQuery);
    }

    /**
     * 분리된 쿼리문을 키와 값의 자료구조로 만듭니다.
     *
     * <p> 퍼센트 인코딩되는 "@" 문자를 그대로 저장합니다.
     *
     * @param splitQuery 분리된 쿼리문
     * @return 파라미터 (Map)
     */
    private static Map<String, String> getParamMap(String[] splitQuery) {
        Map<String, String> params = new HashMap<>();

        for (String query : splitQuery) {
            String[] split = query.split("=", -1);
            if (split[1].contains("%40")) {
                split[1] = split[1].replace("%40", "@");
            }
            params.put(split[0], split[1]);
        }

        return params;
    }
}
