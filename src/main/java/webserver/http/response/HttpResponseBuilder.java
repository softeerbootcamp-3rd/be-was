package webserver.http.response;

import webserver.http.response.HttpResponse;

public class HttpResponseBuilder {
    private static final int SUCCESS_STATUS_CODE = 200;
    private static final int ERROR_STATUS_CODE = 400;
    public HttpResponse createSuccessResponse(byte[] body) {
        HttpResponse response = new HttpResponse(SUCCESS_STATUS_CODE);
        response.addHeader("Content-Type", "text/html;charset=utf-8");
        response.addHeader("Content-Length", String.valueOf(body.length));
        response.setBody(body);
        return response;
    }

    public HttpResponse createErrorResponse(byte[] body) {
        HttpResponse response = new HttpResponse(ERROR_STATUS_CODE);
        response.addHeader("Content-Type", "text/plain;charset=utf-8");
        response.addHeader("Content-Length", String.valueOf(body.length));
        response.setBody(body);
        return response;
    }
}