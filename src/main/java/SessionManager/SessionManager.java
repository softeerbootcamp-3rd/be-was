package SessionManager;

import HTTPModel.Session;

public class SessionManager {
    private static final SessionManager instance = new SessionManager();

    private Session session;

    private SessionManager() {
        // private constructor to enforce singleton pattern
        this.session = new Session();
    }

    public static SessionManager getInstance() {
        return instance;
    }

    public Session getSession() {
        return session;
    }
}
