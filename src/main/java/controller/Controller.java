package controller;

import request.http.HttpRequest;
import response.HttpResponse;

public interface Controller {
    HttpResponse handleUserRequest(HttpRequest httpRequest) throws Exception;
}
