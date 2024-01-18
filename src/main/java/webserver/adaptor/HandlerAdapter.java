package webserver.adaptor;

import http.Request;
import http.Response;
import webserver.ModelAndView;

import java.io.IOException;

public interface HandlerAdapter {
    boolean supports(Object handler);

    ModelAndView handle(Request req, Response res, Object handler) throws IOException;
}
