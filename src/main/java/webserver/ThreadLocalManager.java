package webserver;

import session.Session;
import session.SessionManager;

public class ThreadLocalManager {
    private static final ThreadLocal<String> sessionIdThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<Session> sessionThreadLocal = new ThreadLocal<>();

    public static void setSession(String sessionId) {
        sessionIdThreadLocal.set(sessionId);
        sessionThreadLocal.set(SessionManager.getSession(sessionId));
    }

    public static String getSessionId() {
        return sessionIdThreadLocal.get();
    }

    public static Session getSession() {
        return sessionThreadLocal.get();
    }

    public static void clear() {
        sessionIdThreadLocal.remove();
        sessionThreadLocal.remove();
    }
}
