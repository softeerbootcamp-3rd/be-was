package controller;

import config.AppConfig;
import model.HttpRequest;
import model.Qna;
import service.QnaService;
import util.SessionManager;

import java.util.Collection;

public class DynamicPageLoader {
    private static final QnaService qnaService = AppConfig.qnaService();

    public static void beforeRender(HttpRequest httpRequest, ModelView mv) {
        if (httpRequest.getURI().contains("index.html")) {
            Collection<Qna> allQnas = qnaService.findAllQnas();
            mv.addAttribute("{{qna-list}}", allQnas);

            String sid = httpRequest.getCookie("sid");
            if (!SessionManager.isLoggedIn(sid)) {
                mv.addAttribute("{{welcome}}", "");
            } else {
                mv.addAttribute("{{welcome}}", SessionManager.getSessionById(sid).getName());
            }
        }
    }
}
