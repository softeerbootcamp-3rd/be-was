package webserver;

import session.Session;

public class ThreadLocalManager {
    private static final ThreadLocal<Session> sessionThreadLocal = new ThreadLocal<>();

    public static void setSession(Session session) {
        sessionThreadLocal.set(session);
    }

    public static Session getSession() {
        return sessionThreadLocal.get();
    }

    public static Object getSessionAttribute(String attributeName) {
        return sessionThreadLocal.get().getAttribute(attributeName);
    }

    public static void clear() {
        sessionThreadLocal.remove();
    }
}
