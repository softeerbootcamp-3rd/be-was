package factory;

import dto.HttpResponseDto;
import model.http.Body;
import model.http.ContentType;
import model.http.Status;
import model.http.response.HttpResponse;
import model.http.response.ResponseHeaders;
import model.http.response.StatusLine;

public class HttpResponseFactoryImpl implements HttpResponseFactory {

    public static HttpResponseFactory getInstance() {
        return HttpResponseFactoryHolder.INSTANCE;
    }

    private static class HttpResponseFactoryHolder {
        private static final HttpResponseFactory INSTANCE = new HttpResponseFactoryImpl();
    }

    @Override
    public HttpResponse create(HttpResponseDto responseDto) {
        StatusLine statusLine = getStatusLine(responseDto.getVersion(), responseDto.getStatus());
        ResponseHeaders responseHeaders = getResponseHeaders(responseDto);
        Body body = getBody(responseDto);
        return new HttpResponse(statusLine, responseHeaders, body);
    }

    private Body getBody(HttpResponseDto responseDto) {
        if (responseDto.getContent() == null) {
            return null;
        }
        return new Body(responseDto.getContent());
    }

    private ResponseHeaders getResponseHeaders(HttpResponseDto dto) {
        ResponseHeaders responseHeaders = new ResponseHeaders(dto.getContentType(), dto.getContentLength(), dto.getCharSet());
        if (dto.getLocation() != null) {
            responseHeaders.setLocation(dto.getLocation());
        }
        return responseHeaders;
    }

    public StatusLine getStatusLine(String version, Status status) {
        return new StatusLine(version, status);
    }
}