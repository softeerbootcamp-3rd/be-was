package controller;

import model.User;
import request.HttpRequest;
import response.HttpResponse;
import response.HttpResponseStatus;
import service.MemberLoginService;
import session.SessionManager;

import java.util.HashMap;
import java.util.Map;

public class MemberLoginController extends CrudController{

    MemberLoginService memberLoginService = new MemberLoginService();
    SessionManager sessionManager = new SessionManager();

    @Override
    public void doPost(HttpRequest request, HttpResponse response) {
        String userId = request.getParams().get("userId");
        String password = request.getParams().get("password");

        User loginUser = memberLoginService.login(userId, password);
        if (loginUser == null) { // 로그인 실패 시 로그인 실패 창으로
            responseHeaders.put(LOCATION, "/user/login_failed.html");
            response.setResponse(HttpResponseStatus.FOUND, null, responseHeaders);
        } else { // 로그인 성공 시 홈으로
            String sessionId = sessionManager.createSession(loginUser);

            responseHeaders.put(LOCATION, "/index.html");
            responseHeaders.put(SET_COOKIE, "sid="+sessionId+"; Path=/");
            response.setResponse(HttpResponseStatus.FOUND, null, responseHeaders);
        }
    }
}
