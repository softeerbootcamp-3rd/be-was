package http;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    private static final Logger logger = LoggerFactory.getLogger(SessionManager.class);

    private static final SessionManager instance = new SessionManager();

    public static SessionManager getInstance(){
        return instance;
    }

    private static Map<String, Object> sessionStore = new ConcurrentHashMap<>();


    public static void createSession(Object value, Response response, String cookieName) {
        String sessionId = UUID.randomUUID().toString();
        sessionStore.put(sessionId, value);
        Cookie mySessionCookie = new Cookie(cookieName, sessionId);
        response.addCookie(mySessionCookie);
        response.addCookie(new Cookie("Path","/"));
    }


    public static List<User> getLoginedUsers() {
        List<User> userList = new ArrayList<>();

        for (Object value : sessionStore.values()) {
            if (value instanceof User) {
                userList.add((User) value);
            }
        }
        logger.debug("userList = {}",userList);
        return userList;
    }

    public static Object getSession(Request request, String cookieName) {
        Cookie sessionCookie = findCookie(request, cookieName);
        if (sessionCookie == null) {
            return null;
        }

        return sessionStore.get(sessionCookie.getValue());
    }


    public void expire(Request request,String cookieName) {

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
