package factory;

import model.http.Body;
import model.http.ContentType;
import model.http.Status;
import model.http.request.HttpRequest;
import model.http.response.HttpResponse;
import model.http.response.ResponseHeaders;
import model.http.response.StatusLine;

public class HttpResponseFactoryImpl implements HttpResponseFactory {
    private volatile static HttpResponseFactory httpResponseFactory = null;

    public static HttpResponseFactory getInstance() {
        if (httpResponseFactory == null) {
            synchronized (HttpResponseFactory.class) {
                if (httpResponseFactory == null) {
                    httpResponseFactory = new HttpResponseFactoryImpl();
                }
            }
        }
        return httpResponseFactory;
    }
    public static final String HTTP_VERSION = "HTTP/1.1";
    @Override
    public HttpResponse create(HttpRequest httpRequest, byte[] body){
            StatusLine statusLine = getStatusLine(Status.OK);
            ResponseHeaders responseHeaders = getResponseHeaders(httpRequest, body.length);
            Body responseBody = getBody(body);
        return new HttpResponse(statusLine, responseHeaders, responseBody);
    }
    public Body getBody(byte[] body) {
        return new Body(body);
    }
    private ResponseHeaders getResponseHeaders(HttpRequest httpRequest, int length) {
        if(httpRequest.getHeaders().getAccept().contains("css")){
            return new ResponseHeaders(ContentType.CSS, length);
        }
        if(httpRequest.getHeaders().getAccept().contains("js")){
            return new ResponseHeaders(ContentType.JAVASCRIPT, length);
        }
        if(httpRequest.getHeaders().getAccept().contains("html")){
            return new ResponseHeaders(ContentType.HTML, length);
        }
        return new ResponseHeaders(ContentType.MIME, length);
    }
    private StatusLine getStatusLine(Status status) {
        return new StatusLine(HTTP_VERSION, status);
    }
}