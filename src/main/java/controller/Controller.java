package controller;

import request.HttpRequest;
import response.HttpResponse;

import java.io.IOException;

public interface Controller {
    HttpResponse handleUserRequest(HttpRequest httpRequest) throws IOException;
}
