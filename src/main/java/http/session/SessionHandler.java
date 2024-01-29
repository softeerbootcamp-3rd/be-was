package http.session;

import model.User;
import http.request.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionHandler {
    private static final Logger logger = LoggerFactory.getLogger(SessionHandler.class);

    public User checkSession(HttpRequest request) {
        String sessionId = request.getSessionId();

        if (sessionId != null) {
            User user = SessionStorage.getInstance().getUserBySessionId(sessionId);
            return user;
        }
        return null;
    }
}
