package controller.adapter;

import controller.ModelView;
import model.Request;

import java.io.IOException;

public interface HandlerAdapter {

    boolean supports(Object handler);

    ModelView handle(Request request, Object handler) throws IOException;
}

