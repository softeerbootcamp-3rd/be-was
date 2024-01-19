package controller;

import dto.HttpResponseDto;
import model.http.request.HttpRequest;

public interface UserController {
    void doGet(HttpRequest httpRequest, HttpResponseDto httpResponseDto);
}
