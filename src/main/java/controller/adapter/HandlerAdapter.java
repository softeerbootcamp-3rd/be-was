package controller.adapter;

import controller.ModelView;
import model.Request;
import model.Response;

import java.io.IOException;

public interface HandlerAdapter {

    boolean supports(Object handler);

    ModelView handle(Request request, Response response, Object handler) throws IOException;
}

