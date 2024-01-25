package controller;

import request.HttpRequest;
import response.HttpResponse;

import java.util.HashMap;
import java.util.Map;

public abstract class CrudController implements Controller {
    Map<String, String> responseHeaders = new HashMap<>();

    protected final String LOCATION = "Location";
    protected final String CONTENT_TYPE = "Content-Type";
    protected final String CONTENT_LENGTH = "Content-Length";
    protected final String SET_COOKIE = "set-cookie";



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
