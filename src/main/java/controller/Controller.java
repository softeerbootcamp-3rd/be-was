package controller;

import request.http.HttpRequest;
import response.http.HttpResponse;

public interface Controller {
    HttpResponse handleUserRequest(HttpRequest httpRequest) throws Exception;
}
