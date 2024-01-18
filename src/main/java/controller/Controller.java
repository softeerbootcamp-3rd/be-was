package controller;

import http.Request;

import java.util.Map;

public interface Controller {
    String process(Request request, Map<String, Object> model);
}
