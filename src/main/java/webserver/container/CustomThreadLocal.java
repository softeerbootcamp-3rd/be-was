package webserver.container;

import common.http.request.HttpRequest;
import common.http.response.HttpResponse;
import common.http.response.HttpStatusCode;
import java.util.Map;

public class CustomThreadLocal {
    private static final ThreadLocal<HttpResponse> httpResponseThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<HttpRequest> httpRequestThreadLocal = new ThreadLocal<>();

    public static void setHttpResponse(HttpResponse httpResponse) {
        httpResponseThreadLocal.set(httpResponse);
    }

    public static void setHttpRequest(HttpRequest httpRequest) {
        httpRequestThreadLocal.set(httpRequest);
    }

    public static HttpResponse getHttpResponse() {
        return httpResponseThreadLocal.get();
    }

    public static HttpRequest getHttpRequest() {
        return httpRequestThreadLocal.get();
    }

    public static void onSuccess(HttpStatusCode httpStatusCode, Map<String, String> header, byte[] body) {
        HttpResponse httpResponse = httpResponseThreadLocal.get();
        httpResponse.setStartLine(httpStatusCode);
        httpResponse.setHeader(header);
        httpResponse.setBody(body);
    }

    public static void onFailure(HttpStatusCode httpStatusCode, Map<String, String> header, byte[] body) {
        HttpResponse httpResponse = httpResponseThreadLocal.get();
        httpResponse.setStartLine(httpStatusCode);
        httpResponse.setHeader(header);
        httpResponse.setBody(body);
    }

    public static void clearHttpResponse() {
        httpResponseThreadLocal.remove();
    }

    public static void clearHttpRequest() {
        httpRequestThreadLocal.remove();
    }
}

