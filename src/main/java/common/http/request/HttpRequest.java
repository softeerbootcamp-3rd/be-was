package common.http.request;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private StartLine startLine;
    private Header header;
    private Body body;

    public HttpRequest(StartLine startLine,
        Header header, Body body) {
        this.startLine = startLine;
        this.header = header;
        this.body = body;
    }

    public StartLine getHttpRequestStartLine() {
        return startLine;
    }

    public Header getHttpRequestHeader() {
        return header;
    }

    public Body getHttpRequestBody() {
        return body;
    }

    public String parsingUrl() {
        String requestTarget = startLine.getRequestTarget();
        return requestTarget.split("\\?")[0];
    }

    public Map<String, String> parsingParams() {
        String requestTarget = startLine.getRequestTarget();
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
