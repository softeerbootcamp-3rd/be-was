package controller;

import model.User;
import request.HttpRequest;
import response.HttpResponse;
import response.HttpResponseStatus;
import service.MemberLoginService;

import java.util.HashMap;
import java.util.Map;

public class MemberLoginController implements Controller{

    MemberLoginService memberLoginService = new MemberLoginService();

    @Override
    public void process(HttpRequest request, HttpResponse response) {
        Map<String, String> headers = new HashMap<>();

        String userId = request.getParams().get("userId");
        String password = request.getParams().get("password");

        User loginUser = memberLoginService.login(userId, password);
        if (loginUser == null) { // 로그인 실패 시 다시 로그인 창으로
            headers.put("Location", "/user/login.html");
            response.setResponse(HttpResponseStatus.FOUND, null, headers);
        } else { // 로그인 성공 시 홈으로
            headers.put("Location", "/index.html");
            response.setResponse(HttpResponseStatus.FOUND, null, headers);
        }
    }
}
