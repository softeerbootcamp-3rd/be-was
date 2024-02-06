package webserver.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SessionScheduler {
    private static final Logger logger = LoggerFactory.getLogger(SessionScheduler.class);
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void registerSessionScheduler(){
        scheduler.scheduleAtFixedRate(SessionScheduler::removeExpiredSessions, 0, 1, TimeUnit.MINUTES);
    }

    private static void removeExpiredSessions(){
        List<Session> expiredSessions = SessionDatabase.findExpiredSession();

        expiredSessions.forEach((session) -> SessionDatabase.deleteSession(session.getId()));
        logger.debug("[Session Scheduler] 만료된 세션 삭제");
    }
}
