package service;

import model.http.response.HttpResponse;

import java.io.OutputStream;

public interface HttpResponseSendService {
    void sendHttpResponse(OutputStream out, HttpResponse httpResponse);
}
