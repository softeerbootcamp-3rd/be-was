package controller;

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
        memberJoinService.createUser(request.getParams());

        Map<String, String> headers = new HashMap<>();
        headers.put("Location", "/index.html");
        response.setResponse(HttpResponseStatus.FOUND, null, headers);
    }
}
