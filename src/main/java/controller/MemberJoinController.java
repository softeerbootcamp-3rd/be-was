package controller;

import db.Database;
import request.HttpRequest;
import response.HttpResponse;
import response.HttpResponseStatus;
import service.MemberJoinService;

import java.util.HashMap;
import java.util.Map;

public class MemberJoinController extends CrudController {
    MemberJoinService memberJoinService = new MemberJoinService();
    @Override
    public void doPost(HttpRequest request, HttpResponse response) {
        Map<String, String> params = request.getParams();

        if (memberJoinService.createUser(params)) { // 회원 가입 성공 시 홈으로
            responseHeaders.put(LOCATION, "/index.html");
            response.setResponse(HttpResponseStatus.FOUND, null, responseHeaders);
        } else { // 회원 가입 실패 시 다시 회원 가입 창으로
            responseHeaders.put(LOCATION, "/user/form.html");
            response.setResponse(HttpResponseStatus.FOUND, null, responseHeaders);
        }
    }
}
