package utils;

import constants.StatusCode;
import java.util.Map;
import model.Response;

public class HeaderBuilder {

    /**
     * 응답 메시지의 시작 줄과 헤더를 문자열로 만듭니다.
     *
     * @param response 요청을 수행한 결과 응답
     * @return 응답 메시지의 수작 줄과 헤더 문자열
     */
    public static String build(Response response) {
        StringBuilder header = new StringBuilder(
                "HTTP/1.1 " + response.getCode() + " " + StatusCode.getCodeName(response.getCode())
                        + " \r\n");
        Map<String, String> headerKey = response.getHeaderKey();
        for (String key : headerKey.keySet()) {
            header.append(key).append(": ").append(headerKey.get(key)).append(" \r\n");
        }
        header.append("\r\n");

        return header.toString();
    }
}
