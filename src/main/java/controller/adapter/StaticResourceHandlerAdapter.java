package controller.adapter;

import controller.ModelView;
import controller.StaticResourceController;
import model.Request;

import java.io.IOException;

public class StaticResourceHandlerAdapter implements HandlerAdapter {
    @Override
    public boolean supports(Object handler) {
        return (handler instanceof StaticResourceController);
    }

    @Override
    public ModelView handle(Request request, Object handler) throws IOException {
        StaticResourceController staticResourceController = (StaticResourceController) handler;
        ModelView mv = staticResourceController.process(request.getURI());
        return mv;
    }
}
