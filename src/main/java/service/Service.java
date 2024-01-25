package service;

import httpmessage.Request.HttpRequest;
import httpmessage.Request.RequestHeader;
import httpmessage.Response.HttpResponse;
import httpmessage.View.View;

import java.io.IOException;

public interface Service {
    void process(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException;
}
