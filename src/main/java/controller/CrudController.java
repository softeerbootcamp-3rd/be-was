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
            doPOST(request, response);
        }
    }

    public void doGet(HttpRequest request, HttpResponse response) {
    }

    public void doPOST(HttpRequest request, HttpResponse response) {
    }

}
