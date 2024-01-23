package controller.adapter;

import controller.ModelView;
import controller.ResourceController;
import model.Request;

import java.io.IOException;

public class ResourceHandlerAdapter implements HandlerAdapter {
    @Override
    public boolean supports(Object handler) {
        return (handler instanceof ResourceController);
    }

    @Override
    public ModelView handle(Request request, Object handler) throws IOException {
        ResourceController resourceController = (ResourceController) handler;
        ModelView mv = resourceController.process(request.getURI());
        return mv;
    }
}
