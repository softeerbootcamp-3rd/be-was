package factory;

import model.http.response.HttpResponse;
import dto.HttpResponseDto;

public interface HttpResponseFactory {
    HttpResponse create(HttpResponseDto httpResponseDto);
}