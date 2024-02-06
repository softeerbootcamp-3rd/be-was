package controller;

import httpmessage.request.HttpRequest;
import httpmessage.response.HttpResponse;

import java.io.IOException;

public interface Controller {
    void service(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException, ClassNotFoundException;

}
