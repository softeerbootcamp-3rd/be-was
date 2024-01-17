package factory;

import model.http.Body;
import model.http.ContentType;
import model.http.Status;
import model.http.response.HttpResponse;
import dto.HttpResponseDto;
import model.http.response.ResponseHeaders;
import model.http.response.StatusLine;

public class HttpResponseFactoryImpl implements HttpResponseFactory {

    public static HttpResponseFactory getInstance() {
        return HttpResponseFactoryHolder.INSTANCE;
    }
    private static class HttpResponseFactoryHolder{
        private static final HttpResponseFactory INSTANCE = new HttpResponseFactoryImpl();
    }
    @Override
    public HttpResponse create(HttpResponseDto responseDto){
        StatusLine statusLine = getStatusLine(responseDto.getVersion(), responseDto.getStatus());
        ResponseHeaders responseHeaders = getResponseHeaders(responseDto.getContentType()
                , responseDto.getContentLength(), responseDto.getCharSet());
        return new HttpResponse(statusLine, responseHeaders, new Body(responseDto.getContent()));
    }
    private ResponseHeaders getResponseHeaders(ContentType contentType, int contentLength, String charSet) {
        return new ResponseHeaders(contentType, contentLength, charSet);
    }
    public StatusLine getStatusLine(String version, Status status) {
        return new StatusLine(version, status);
    }
}