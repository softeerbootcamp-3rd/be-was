package util;

import java.util.Map;

public class HttpRequestHeaderUtils {
    private Map<String, String> requestHeaderMap; // 헤더 <field, value> 저장
    private Map<String, String> requestLineMap;
    private HttpRequestHeaderUtils() {
    }

    public static HttpRequestHeaderUtils createHeaderUtils(String requestHeader){
        HttpRequestHeaderUtils httpRequestHeaderUtils = new HttpRequestHeaderUtils();
        httpRequestHeaderUtils.requestLineMap = HttpRequestHeaderParser.parseRequestLine(requestHeader);
        httpRequestHeaderUtils.requestHeaderMap = HttpRequestHeaderParser.parseHeaders(requestHeader);
        return httpRequestHeaderUtils;
    }

    public String getRequestMethod() {
        return requestLineMap.get("Method");
    }
    public String getRequestUri() {
        return requestLineMap.get("RequestUri");
    }
    public String getQueryString() {
        return  requestLineMap.get("QueryString");
    }
    public Map<String,String> getRequestHeaders() {
        return requestHeaderMap;
    }
}
