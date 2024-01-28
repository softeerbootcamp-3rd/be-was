package controller;

import annotation.PostMapping;
import request.HttpRequest;
import response.HttpResponse;
import response.HttpResponseStatus;
import session.SessionManager;

import java.util.HashMap;
import java.util.Map;

public class MemberLogoutController extends CrudController {
    SessionManager sessionManager = new SessionManager();
    @PostMapping(url = "/user/logout")
    public void doPost(HttpRequest request, HttpResponse response) {
        sessionManager.deleteSession(request);
        responseHeaders.put(LOCATION, "/index.html");
        response.setResponse(HttpResponseStatus.FOUND, null, responseHeaders);
    }
}
