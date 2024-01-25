package controller;

import request.HttpRequest;
import response.HttpResponse;

public abstract class CrudController implements Controller {
    @Override
    public void process(HttpRequest request, HttpResponse response) {
        if (request.getMethod().equals("GET")) {
            doGet(request, response);
        }
        if (request.getMethod().equals("POST")) {
            doPost(request, response);
        }
    }

    public void doGet(HttpRequest request, HttpResponse response) {
    }

    public void doPost(HttpRequest request, HttpResponse response) {
    }

}
