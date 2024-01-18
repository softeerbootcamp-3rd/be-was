package controller;

import request.HttpRequest;
import response.HttpResponse;

public interface Controller {
    void process(HttpRequest request, HttpResponse response);
}
