package handler;

import dto.HttpResponseDto;
import model.http.request.HttpRequest;

public interface StaticResponseHandler {
    public void handle(HttpRequest httpRequest, HttpResponseDto httpResponseDto);
}