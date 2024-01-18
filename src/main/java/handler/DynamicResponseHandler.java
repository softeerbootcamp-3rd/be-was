package handler;

import dto.HttpResponseDto;
import model.http.request.HttpRequest;

public interface DynamicResponseHandler {
    public void handle(HttpRequest httpRequest, HttpResponseDto httpResponseDto);
}