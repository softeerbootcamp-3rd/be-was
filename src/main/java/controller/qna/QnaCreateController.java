package controller.qna;

import controller.ModelView;
import model.Request;
import model.Response;
import service.QnaService;

public class QnaCreateController implements QnaController {
    private final QnaService qnaService;

    public QnaCreateController(QnaService qnaService) {
        this.qnaService = qnaService;
    }

    @Override
    public ModelView process(Request request, Response response) {
        String writer = request.getParameter("writer");
        String title = request.getParameter("title");
        String contents = request.getParameter("contents");

        qnaService.addQna(writer, title, contents);
        response.set302Redirect();
        response.putToHeaderMap("Location", "/index.html");

        return new ModelView("/templates/index.html");
    }
}
