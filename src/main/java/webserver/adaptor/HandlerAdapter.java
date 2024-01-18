package webserver.adaptor;

import controller.Controller;
import http.Request;
import http.Response;
import webserver.ModelAndView;

import java.io.IOException;

public interface HandlerAdapter {
    boolean supports(Controller handler);

    ModelAndView handle(Request req, Response res, Controller handler) throws IOException;
}
