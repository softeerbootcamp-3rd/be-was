package controller.adapter;

import controller.ModelView;
import controller.user.UserController;
import model.Request;
import model.Response;

import java.io.IOException;
import java.util.Map;

public class UserControllerHandlerAdapter implements HandlerAdapter{
    @Override
    public boolean supports(Object handler) {
        return (handler instanceof UserController);
    }

    @Override
    public ModelView handle(Request request, Response response, Object handler) throws IOException {
        UserController controller = (UserController) handler;

        ModelView mv = controller.process(request, response);

        return mv;
    }
}
