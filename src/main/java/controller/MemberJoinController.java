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

        for (String value : params.values()) {
            if(value.equals("")) {
                responseHeaders.put(LOCATION, "/user/form.html");
                response.setResponse(HttpResponseStatus.FOUND, null, responseHeaders);
                return;
            }
        }

        if (Database.findUserById(params.get("userId")) != null) {
            responseHeaders.put(LOCATION, "/user/form.html");
            response.setResponse(HttpResponseStatus.FOUND, null, responseHeaders);
            return;
        }

        memberJoinService.createUser(params);
        responseHeaders.put("Location", "/index.html");
        response.setResponse(HttpResponseStatus.FOUND, null, responseHeaders);
    }
}
