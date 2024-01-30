package util;

import model.User;
import model.UserSession;
import repository.SessionStorage;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class SessionManager {

    public static final long EXPIRED_TIME = 1800;
    private static final TimeUnit EXPIRED_TIME_UNIT = TimeUnit.SECONDS;
    private final static SessionStorage storage = new SessionStorage();


    public static String generateSID() {
        return UUID.randomUUID().toString();
    }

    public static void addSession(String sid, User user) {
        storage.addSession(sid, user, EXPIRED_TIME, EXPIRED_TIME_UNIT);
    }

    public static User getSessionById(String sid) {
        UserSession userSession = storage.getSessionById(sid);
        if (userSession != null) {
            return userSession.getUser();
        }

        throw new IllegalArgumentException(sid + "에 해당하는 Session이 존재하지 않습니다");
    }

    public static boolean isLoggedIn(String sid) {
        UserSession userSession = storage.getSessionById(sid);

        return userSession != null;
    }
}
