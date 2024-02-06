package http.response.handler;

import http.request.HttpRequest;
import http.response.HttpResponse;
import http.session.SessionHandler;
import model.User;
import http.response.renderer.HtmlResponseRenderer;
import utils.FileUtils;

import java.io.IOException;

public class LoginResponseHandler {

    private final SessionHandler sessionHandler;

    public LoginResponseHandler() {
        this.sessionHandler = new SessionHandler();
    }

    public void handle(HttpRequest request, HttpResponse response) {
        User loggedInUser = sessionHandler.checkSession(request);

        if (request.getPath().equals("/index.html") || request.getPath().equals("/")) {
            handleIndexRequest(request, response, loggedInUser);
            if (loggedInUser != null) {
                //logger.debug("로그인시 바디 : {}", responseBody);
            }
        }
    }

    private void handleIndexRequest(HttpRequest request, HttpResponse response, User loggedInUser) {
        try {
            byte[] responseBody;
            if (loggedInUser != null) {
                // 로그인 상태일 때 사용자 이름을 포함한 HTML 생성
                String htmlWithUserName = HtmlResponseRenderer.renderHtmlWithUserName("templates/index.html", loggedInUser.getName());
                responseBody = htmlWithUserName.getBytes();
            } else {
                // 로그인 버튼만 있는 기본 HTML 응답
                String htmlContent = FileUtils.readFileAsString("templates/index.html");
                responseBody = htmlContent.getBytes();
            }
            response.setBody(responseBody);
            response.setHeader("Content-Type", "text/html");
        } catch (IOException e) {
            response.setBody("Error loading page".getBytes());
        }
    }
}
