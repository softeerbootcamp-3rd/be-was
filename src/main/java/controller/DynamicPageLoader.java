package controller;

import config.AppConfig;
import model.HttpRequest;
import model.Qna;
import service.QnaService;
import java.util.Collection;

public class DynamicPageLoader {
    private static final QnaService qnaService = AppConfig.qnaService();

    public static void postHandle(HttpRequest httpRequest, ModelView mv) {
        if (httpRequest.getURI().contains("index.html")) {
            Collection<Qna> allQnas = qnaService.findAllQnas();
            mv.addAttribute("{{qna-list}}", allQnas);
        }
    }
}
