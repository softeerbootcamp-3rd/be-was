package webserver.adaptor;

import controller.BasicController;
import http.Request;
import http.Response;
import webserver.ModelAndView;

public interface HandlerAdapter {
    boolean supports(BasicController handler);

    ModelAndView handle(Request req, Response res, BasicController handler);
}
