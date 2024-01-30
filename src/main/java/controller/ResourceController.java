package controller;

import annotation.GetMapping;
import model.User;
import request.HttpRequest;
import response.HttpResponse;
import response.HttpResponseBuilder;
import response.HttpResponseStatus;
import session.SessionManager;
import view.ViewResolver;

import java.util.HashMap;
import java.util.Map;

public class ResourceController {
    Map<String, String> responseHeaders = new HashMap<>();
    final String CONTENT_TYPE = "Content-Type";
    final String CONTENT_LENGTH = "Content-Length";

    static Map<String, String> contentType = new HashMap<>();
    SessionManager sessionManager = new SessionManager();

    static {
        contentType.put("html", "text/html; charset=utf-8");
        contentType.put("css", "text/css; charset=utf-8");
        contentType.put("js", "text/javascript; charset=utf-8");
        contentType.put("png", "image/png");
        contentType.put("jpg", "image/jpg");
        contentType.put("ico", "image/x-icon");
    }
    @GetMapping(url = "/resources")
    public HttpResponse getResources(HttpRequest request) {
        Map<String, Object> model = new HashMap<>();
        if(request.isAuth()) {
            User loginUser = sessionManager.getUserBySessionId(request);
            model.put("name", loginUser.getName());
        }

        byte[] body = ViewResolver.resolve(request.getUrl(), request.isAuth(), model);
        responseHeaders.put(CONTENT_TYPE, getContentType(request.getUrl()));
        responseHeaders.put(CONTENT_LENGTH, String.valueOf(body.length));
        return new HttpResponseBuilder().status(HttpResponseStatus.OK)
                .headers(responseHeaders)
                .body(body)
                .build();
    }
    private String getContentType(String url) {
        String extension = url.substring(url.lastIndexOf(".") + 1);
        String result = contentType.get(extension);
        if (result == null) {
            return "font/" + extension;
        }
        return result;
    }
}
