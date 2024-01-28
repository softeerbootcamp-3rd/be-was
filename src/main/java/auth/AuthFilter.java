package auth;

import http.HttpRequest;

public class AuthFilter {

    SessionManager sessionManager;

    public AuthFilter() {
        sessionManager = SessionManager.getInstance();
    }

    public void doFilter(HttpRequest request) {
        String userId;

        if (request.getSessionId() != null) {
            String sessionId = request.getSessionId();
            int startIndex = sessionId.indexOf("sid=");
            if (startIndex != -1) {
                sessionId = sessionId.substring(startIndex + 4);
            }
            userId = SessionManager.getUserBySessionId(sessionId);
            request.setUserId(userId);
        }
    }
}
