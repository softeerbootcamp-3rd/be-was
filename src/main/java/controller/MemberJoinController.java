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

        if(validation(params)) {
            memberJoinService.createUser(params);
            responseHeaders.put(LOCATION, "/index.html");
            response.setResponse(HttpResponseStatus.FOUND, null, responseHeaders);
        } else {
            responseHeaders.put(LOCATION, "/user/form.html");
            response.setResponse(HttpResponseStatus.FOUND, null, responseHeaders);
        }
    }

    private static boolean validation(Map<String, String> params) {
        boolean valid = true;
        for (String value : params.values()) {
            if(value.equals("")) {
                return false;
            }
        }
        if (Database.findUserById(params.get("userId")) != null) {
            return false;
        }
        return true;
    }
}
