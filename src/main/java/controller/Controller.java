package controller;

import http.request.HttpMethod;
import http.request.Request;
import http.response.Response;

public abstract class Controller {
    public void service(Request request, Response response) {
        if (request.getMethod() == HttpMethod.GET) {
            doGet(request, response);
        }
        if (request.getMethod() == HttpMethod.POST) {
            doPost(request, response);
        }
    }

    public void doGet(Request request, Response response) {
    }

    public void doPost(Request request, Response response) {
    }
}
