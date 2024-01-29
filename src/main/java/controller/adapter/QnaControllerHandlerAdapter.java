package controller.adapter;

import controller.ModelView;
import controller.qna.QnaController;
import controller.user.UserController;
import model.Request;
import model.Response;

import java.io.IOException;

public class QnaControllerHandlerAdapter implements HandlerAdapter{
    @Override
    public boolean supports(Object handler) {
        return (handler instanceof QnaController);
    }

    @Override
    public ModelView handle(Request request, Response response, Object handler) throws IOException {
        QnaController controller = (QnaController) handler;

        ModelView mv = controller.process(request, response);

        return mv;
    }
}
