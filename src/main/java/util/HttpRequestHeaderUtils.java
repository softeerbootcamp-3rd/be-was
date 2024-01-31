package util;

import java.util.Map;

public class HttpRequestHeaderUtils {
	private Map<String, String> requestLineMap;
    private Map<String, String> requestHeaderMap; // 헤더 <field, value> 저장
    private HttpRequestHeaderUtils() {
    }

    public static HttpRequestHeaderUtils createHeaderUtils(String requestHeader){
        HttpRequestHeaderUtils httpRequestHeaderUtils = new HttpRequestHeaderUtils();
        httpRequestHeaderUtils.requestLineMap = HttpRequestHeaderParser.parseRequestLine(requestHeader);
        httpRequestHeaderUtils.requestHeaderMap = HttpRequestHeaderParser.parseHeaders(requestHeader);
        return httpRequestHeaderUtils;
    }

    public String getRequestMethod() {
        return requestLineMap.get("method");
    }
    public String getRequestUri() {
        return requestLineMap.get("requestUri");
    }
    public String getQueryString() {
        return  requestLineMap.get("queryString");
    }
    public Map<String,String> getRequestHeaders() {
        return requestHeaderMap;
    }
}
