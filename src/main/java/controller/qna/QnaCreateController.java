package controller.qna;

import controller.ModelView;
import model.HttpRequest;
import model.HttpResponse;
import service.QnaService;

public class QnaCreateController implements QnaController {
    private final QnaService qnaService;

    public QnaCreateController(QnaService qnaService) {
        this.qnaService = qnaService;
    }

    @Override
    public ModelView process(HttpRequest httpRequest, HttpResponse httpResponse) {
        String writer = httpRequest.getParameter("writer");
        String title = httpRequest.getParameter("title");
        String contents = httpRequest.getParameter("contents");

        qnaService.addQna(writer, title, contents);
        httpResponse.set302Redirect();
        httpResponse.putToHeaderMap("Location", "/index.html");

        return new ModelView("/templates/index.html");
    }
}
