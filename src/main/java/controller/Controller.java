package controller;

import http.HttpRequest;
import http.HttpResponse;

public interface Controller {

    String process(HttpRequest request, HttpResponse response);
}
