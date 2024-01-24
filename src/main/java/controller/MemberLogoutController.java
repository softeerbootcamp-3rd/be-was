package controller;

import request.HttpRequest;
import response.HttpResponse;
import response.HttpResponseStatus;
import session.SessionManager;

import java.util.HashMap;
import java.util.Map;

public class MemberLogoutController implements Controller {
    SessionManager sessionManager = new SessionManager();
    @Override
    public void process(HttpRequest request, HttpResponse response) {
        sessionManager.deleteSession(request);
        Map<String, String> headers = new HashMap<>();
        headers.put("Location", "/index.html");
        response.setResponse(HttpResponseStatus.FOUND, null, headers);
    }
}
