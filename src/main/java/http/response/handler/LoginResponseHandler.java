package http.response.handler;

import db.Database;
import http.request.HttpRequest;
import http.response.HttpResponse;
import http.session.SessionHandler;
import model.User;
import http.response.renderer.HtmlResponseRenderer;


public class LoginResponseHandler {

    private final SessionHandler sessionHandler;

    public LoginResponseHandler() {
        this.sessionHandler = new SessionHandler();
    }

    public void handle(HttpRequest request, HttpResponse response) {
        User loggedInUser = sessionHandler.checkSession(request);
        handleIndexRequest(request, response, loggedInUser);
    }

    private void handleIndexRequest(HttpRequest request, HttpResponse response, User loggedInUser) {
        String htmlContent = Database.getHomePageHtml(); // 데이터베이스에서 현재 저장된 HTML 가져오기

        if (loggedInUser != null) {
            // 로그인한 사용자의 이름을 포함시켜 HTML 수정
            htmlContent = HtmlResponseRenderer.renderHtmlWithUserName(htmlContent, loggedInUser.getName());
        } else {
            // 로그아웃 상태에 맞는 HTML 수정
            htmlContent = HtmlResponseRenderer.renderHtmlForLoggedOutState(htmlContent);
        }

        // 데이터베이스에 업데이트된 HTML 저장
        Database.setHomePageHtml(htmlContent);
    }
}
