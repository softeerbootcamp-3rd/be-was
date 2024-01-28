package webserver.http.response;

import webserver.http.response.enums.HttpStatus;

public class HttpResponseBuilder {

    private static final HttpResponseBuilder instance = new HttpResponseBuilder();

    private HttpResponseBuilder() {}

    public static HttpResponseBuilder getInstance(){
        return instance;
    }

    public HttpResponse createSuccessResponse(HttpStatus status, byte[] body) {
        HttpResponse response = new HttpResponse(status);
        response.addHeader("Content-Type", "text/html;charset=utf-8");
        response.addHeader("Content-Length", String.valueOf(body.length));
        response.setBody(body);
        return response;
    }

    public HttpResponse createErrorResponse(HttpStatus status, byte[] body) {
        HttpResponse response = new HttpResponse(status);
        response.addHeader("Content-Type", "text/plain;charset=utf-8");
        response.addHeader("Content-Length", String.valueOf(body.length));
        response.setBody(body);
        return response;
    }

    public HttpResponse createRedirectResponse(HttpStatus status, String redirectPath) {
        HttpResponse response = new HttpResponse(status);
        response.addHeader("Location", redirectPath);
        return response;
    }

    public void addSessionCookie(HttpResponse response, String sessionId){
        String cookieValue = String.format("sid=%s; Path=/", sessionId);
        response.addHeader("Set-Cookie", cookieValue);
    }
}
