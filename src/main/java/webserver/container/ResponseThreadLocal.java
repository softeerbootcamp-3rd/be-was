package webserver.container;

import common.http.response.HttpResponse;
import common.http.response.HttpStatusCode;
import java.util.HashMap;

public class ResponseThreadLocal {
    private static final ThreadLocal<HttpResponse> httpResponseThreadLocal = new ThreadLocal<>();

    public static void setHttpResponse(HttpResponse httpResponse) {
        httpResponseThreadLocal.set(httpResponse);
    }

    public static HttpResponse getHttpResponse() {
        return httpResponseThreadLocal.get();
    }

    public static void onSuccess(HttpStatusCode httpStatusCode, HashMap<String, String> header, byte[] body) {
        HttpResponse httpResponse = httpResponseThreadLocal.get();
        httpResponse.setStartLine(httpStatusCode);
        httpResponse.setHeader(header);
        httpResponse.setBody(body);
    }

    public static void onFailure(HttpStatusCode httpStatusCode, HashMap<String, String> header, byte[] body) {
        HttpResponse httpResponse = httpResponseThreadLocal.get();
        httpResponse.setStartLine(httpStatusCode);
        httpResponse.setHeader(header);
        httpResponse.setBody(body);
    }

    public static void clearHttpResponse() {
        httpResponseThreadLocal.remove();
    }
}

