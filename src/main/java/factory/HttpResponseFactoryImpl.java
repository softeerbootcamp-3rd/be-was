package factory;

import dto.HttpResponseDto;
import model.http.Body;
import model.http.Status;
import model.http.response.HttpResponse;
import model.http.response.ResponseHeaders;
import model.http.response.StatusLine;

import java.util.HashMap;
import java.util.Map;

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
        String content = responseDto.getContent();
        return content != null ? new Body(content) : null;
    }

    private ResponseHeaders getResponseHeaders(HttpResponseDto dto) {
        ResponseHeaders responseHeaders = new ResponseHeaders(dto.getContentType(), dto.getContentLength(), dto.getCharSet());
        dto.getOptionHeader().forEach(responseHeaders::addOptionHeader);
        return responseHeaders;
    }

    private StatusLine getStatusLine(String version, Status status) {
        return new StatusLine(version, status);
    }
}
