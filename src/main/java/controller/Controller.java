package controller;

import request.HttpRequest;
import response.HttpResponse;

public interface Controller {
    public void process(HttpRequest request, HttpResponse response);
}
