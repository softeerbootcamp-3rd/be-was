package factory;

import dto.HttpResponseDto;
import model.http.response.HttpResponse;

public interface HttpResponseFactory {
    HttpResponse create(HttpResponseDto httpResponseDto);
}