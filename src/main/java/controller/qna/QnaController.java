package controller.qna;

import controller.ModelView;
import model.HttpRequest;
import model.HttpResponse;

public interface QnaController {
    ModelView process(HttpRequest httpRequest, HttpResponse httpResponse);

}
