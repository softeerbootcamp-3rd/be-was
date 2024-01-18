package webApplicationServer.controller;

import dto.HttpResponseDto;
import model.http.request.HttpRequest;
import model.http.response.HttpResponse;

public interface UserController {
    void doGet(HttpRequest httpRequest, HttpResponseDto httpResponseDto);
}