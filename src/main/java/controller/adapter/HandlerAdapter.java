package controller.adapter;

import controller.ModelView;
import model.HttpRequest;
import model.HttpResponse;

import java.io.IOException;

public interface HandlerAdapter {

    boolean supports(Object handler);

    ModelView handle(HttpRequest httpRequest, HttpResponse httpResponse, Object handler) throws IOException;
}

