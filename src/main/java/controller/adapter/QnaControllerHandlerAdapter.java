package controller.adapter;

import controller.ModelView;
import controller.qna.QnaController;
import model.HttpRequest;
import model.HttpResponse;

import java.io.IOException;

public class QnaControllerHandlerAdapter implements HandlerAdapter{
    @Override
    public boolean supports(Object handler) {
        return (handler instanceof QnaController);
    }

    @Override
    public ModelView handle(HttpRequest httpRequest, HttpResponse httpResponse, Object handler) throws IOException {
        QnaController controller = (QnaController) handler;

        ModelView mv = controller.process(httpRequest, httpResponse);

        return mv;
    }
}
