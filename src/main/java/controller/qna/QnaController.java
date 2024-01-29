package controller.qna;

import controller.ModelView;
import model.Request;
import model.Response;

public interface QnaController {
    ModelView process(Request request, Response response);

}
