package controller;

import annotation.GetMapping;
import annotation.PostMapping;
import annotation.RequestParam;
import request.HttpRequest;
import session.SessionManager;

public class PostController {
    SessionManager sessionManager = new SessionManager();

    @GetMapping(url = "/write")
    public String getWriteForm(HttpRequest request) {
        if (sessionManager.getUserBySessionId(request) == null) {
            return "/user/login.html";
        } else {
            return "write_form.html";
        }
    }
}
