package webserver.adaptor;

import http.Request;
import http.Response;
import controller.Controller;
import webserver.ModelAndView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MyHandlerAdapter implements HandlerAdapter {
    @Override
    public boolean supports(Object handler) {
        return (handler instanceof Controller);
    }

    @Override
    public ModelAndView handle(Request req, Response res, Object handler) throws IOException {
        Controller controller = (Controller) handler;

        Map<String, Object> model = new HashMap<>();

        String viewName = controller.process(req,model);

        ModelAndView mv = new ModelAndView(viewName);
        mv.setModel(model);

        return mv;
    }
}
