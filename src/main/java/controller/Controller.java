package controller;

import dto.HttpRequestDto;
import dto.HttpResponseDto;

public interface Controller {
    public abstract HttpResponseDto handleRequest(HttpRequestDto request);
}
