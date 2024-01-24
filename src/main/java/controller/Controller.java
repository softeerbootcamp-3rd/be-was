package controller;

import http.HttpRequest;
import http.HttpResponse;

import java.util.Map;

public interface Controller {

    String process(HttpRequest request, HttpResponse response);
}
