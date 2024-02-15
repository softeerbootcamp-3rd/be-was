package repository;

import model.User;
import model.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SessionStorage {
    private final Logger logger = LoggerFactory.getLogger(SessionStorage.class);
    private final Map<String, UserSession> storage = new ConcurrentHashMap<>();
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    public void addSession(String sid, User user, long timeout, TimeUnit unit) {
        UserSession userSession = new UserSession(user, timeout);
        storage.put(sid, userSession);

        executorService.schedule(() -> {
            storage.remove(sid);
            logger.debug("SID: " + sid + " has been expired and removed.");
        }, timeout, unit);
    }

    public UserSession getSessionById(String sid) {
        return storage.get(sid);
    }

}
