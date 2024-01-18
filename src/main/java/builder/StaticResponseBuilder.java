package builder;

import dto.HttpResponseDto;
import model.http.request.HttpRequest;

public interface StaticResponseBuilder {
    public void build(HttpRequest httpRequest, HttpResponseDto httpResponseDto);
}