package controller;

import db.Database;
import request.HttpRequest;
import response.HttpResponse;
import response.HttpResponseStatus;
import service.MemberJoinService;

import java.util.HashMap;
import java.util.Map;

public class MemberJoinController implements Controller {
    MemberJoinService memberJoinService = new MemberJoinService();
    @Override
    public void process(HttpRequest request, HttpResponse response) {
        Map<String, String> headers = new HashMap<>();

        Map<String, String> params = request.getParams();
        for (String value : params.values()) {
            if(value.equals("")) {
                headers.put("Location", "/user/form.html");
                response.setResponse(HttpResponseStatus.FOUND, null, headers);
                return;
            }
        }

        if (Database.findUserById(params.get("userId")) != null) {
            headers.put("Location", "/user/form.html");
            response.setResponse(HttpResponseStatus.FOUND, null, headers);
            return;
        }


        memberJoinService.createUser(params);
        headers.put("Location", "/index.html");
        response.setResponse(HttpResponseStatus.FOUND, null, headers);
    }
}
