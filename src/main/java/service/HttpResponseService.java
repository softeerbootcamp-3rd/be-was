package service;

import model.http.response.HttpResponse;

import java.io.OutputStream;

public interface HttpResponseService {
    void sendHttpResponse(OutputStream out, HttpResponse httpResponse);
}