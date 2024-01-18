package service;

import dto.HttpResponseDto;
import model.http.request.HttpRequest;

public interface DynamicResponseBuilder {
    public void build(HttpRequest httpRequest, HttpResponseDto httpResponseDto);
}