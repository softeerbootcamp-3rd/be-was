package frontController.adaptor;

import dto.Request;
import dto.Response;
import frontController.ModelAndView;

import java.io.IOException;

public interface HandlerAdapter {
    boolean supports(Object handler);

    ModelAndView handle(Request req, Response res, Object handler) throws IOException;
}
