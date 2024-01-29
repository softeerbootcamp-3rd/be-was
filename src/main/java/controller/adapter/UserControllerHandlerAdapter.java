package controller.adapter;

import controller.ModelView;
import controller.user.UserController;
import model.HttpRequest;
import model.HttpResponse;

import java.io.IOException;

public class UserControllerHandlerAdapter implements HandlerAdapter{
    @Override
    public boolean supports(Object handler) {
        return (handler instanceof UserController);
    }

    @Override
    public ModelView handle(HttpRequest httpRequest, HttpResponse httpResponse, Object handler) throws IOException {
        UserController controller = (UserController) handler;

        ModelView mv = controller.process(httpRequest, httpResponse);

        return mv;
    }
}
