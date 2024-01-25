package handler;

import http.HttpRequest;
import http.HttpResponse;


public class HomeRequestHandler implements RequestHandler{
    @Override
    public HttpResponse handle(HttpRequest httpRequest){
        String uri = httpRequest.getUri();
        if (uri.equals("/")) {
            uri = "/index.html";
        }
        return getHttpResponse(uri);
    }
}
