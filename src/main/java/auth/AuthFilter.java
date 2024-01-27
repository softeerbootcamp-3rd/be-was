package auth;

import controller.SessionManager;
import http.HttpRequest;
import model.User;

public class AuthFilter {

    SessionManager sessionManager;

    public AuthFilter() {
        sessionManager = SessionManager.getInstance();
    }

    public void doFilter(HttpRequest request) {
        User user=null;
        if (request.getSessionId() != null) {
            user = sessionManager.getUserBySessionId(request.getSessionId());
            request.setUserId(user.getUserId());
        }
    }
}
