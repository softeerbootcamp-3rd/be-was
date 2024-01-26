package http;

import org.checkerframework.checker.units.qual.C;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    private static final Logger logger = LoggerFactory.getLogger(SessionManager.class);

    private static final SessionManager instance = new SessionManager();

    public static SessionManager getInstance(){
        return instance;
    }

    private static Map<String, Object> sessionStore = new ConcurrentHashMap<>();

    public void createSession(Object value, Response response,String cookieName) {
        String sessionId = UUID.randomUUID().toString();
        sessionStore.put(sessionId, value);
        Cookie mySessionCookie = new Cookie(cookieName, sessionId);
        response.addCookie(mySessionCookie);
        response.addCookie(new Cookie("Path","/"));
    }

    public static Object getSession(Request request, String cookieName) {
        Cookie sessionCookie = findCookie(request,cookieName);
        if (sessionCookie == null) {
            return null;
        }

        return sessionStore.get(sessionCookie.getValue());
    }

    public void expire(Request request, String cookieName) {

        Cookie sessionCookie = findCookie(request, cookieName);
        if (sessionCookie != null) {
            sessionStore.remove(sessionCookie.getValue());
        }
    }
    public static Cookie findCookie(Request request, String cookieName) {
        if (request.getCookies() == null) {
            return null;
        }

        return request.getCookies().stream()
                .filter(cookie -> cookie.getKey().equals(cookieName))
                .findAny()
                .orElse(null);
    }
}
