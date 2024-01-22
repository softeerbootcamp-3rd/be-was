package common.http.request;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private HttpRequestStartLine httpRequestStartLine;
    private HttpRequestHeader httpRequestHeader;
    private HttpRequestBody httpRequestBody;

    public HttpRequest(HttpRequestStartLine httpRequestStartLine,
        HttpRequestHeader httpRequestHeader, HttpRequestBody httpRequestBody) {
        this.httpRequestStartLine = httpRequestStartLine;
        this.httpRequestHeader = httpRequestHeader;
        this.httpRequestBody = httpRequestBody;
    }

    public HttpRequestStartLine getHttpRequestStartLine() {
        return httpRequestStartLine;
    }

    public HttpRequestHeader getHttpRequestHeader() {
        return httpRequestHeader;
    }

    public HttpRequestBody getHttpRequestBody() {
        return httpRequestBody;
    }

    public String parsingUrl() {
        String requestTarget = httpRequestStartLine.getRequestTarget();
        return requestTarget.split("\\?")[0];
    }

    public Map<String, String> parsingParams() {
        String requestTarget = httpRequestStartLine.getRequestTarget();
        if (!requestTarget.contains("?")) {
            return new HashMap<>();
        }
        String params = requestTarget.split("\\?")[1];

        Map<String, String> paramsMap = new HashMap<>();
        Arrays.stream(params.split("&"))
            .map(param -> param.split("="))
            .forEach(param -> paramsMap.put(param[0], param[1]));
        return paramsMap;
    }
}
