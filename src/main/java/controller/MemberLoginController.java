package controller;

import model.User;
import request.HttpRequest;
import response.HttpResponse;
import response.HttpResponseStatus;
import service.MemberLoginService;
import session.SessionManager;

import java.util.HashMap;
import java.util.Map;

public class MemberLoginController implements Controller{

    MemberLoginService memberLoginService = new MemberLoginService();
    SessionManager sessionManager = new SessionManager();

    @Override
    public void process(HttpRequest request, HttpResponse response) {
        Map<String, String> headers = new HashMap<>();

        String userId = request.getParams().get("userId");
        String password = request.getParams().get("password");

        User loginUser = memberLoginService.login(userId, password);
        if (loginUser == null) { // 로그인 실패 시 로그인 실패 창으로
            headers.put("Location", "/user/login_failed.html");
            response.setResponse(HttpResponseStatus.FOUND, null, headers);
        } else { // 로그인 성공 시 홈으로
            String sessionId = sessionManager.createSession(loginUser);

            headers.put("Location", "/index.html");
            headers.put("set-cookie", "sid="+sessionId+"; Path=/");
            response.setResponse(HttpResponseStatus.FOUND, null, headers);
        }
    }
}
