package controller.adapter;

import controller.ModelView;
import controller.ResourceController;
import model.HttpRequest;
import model.HttpResponse;

import java.io.IOException;

public class ResourceHandlerAdapter implements HandlerAdapter {
    @Override
    public boolean supports(Object handler) {
        return (handler instanceof ResourceController);
    }

    @Override
    public ModelView handle(HttpRequest httpRequest, HttpResponse httpResponse, Object handler) throws IOException {
        ResourceController resourceController = (ResourceController) handler;
        ModelView mv = resourceController.process(httpRequest.getURI());
        return mv;
    }
}
