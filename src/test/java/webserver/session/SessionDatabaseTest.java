package webserver.session;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SessionDatabaseTest {
    @Test
    @DisplayName("세션이 정상적으로 저장되는지 테스트")
    void sessionTest(){
        String userId = "user";
        Session session = SessionManager.createSession(userId);

        SessionDatabase.addSession(session);

        Session sessionFromDatabase = SessionDatabase.loadSession(session.getId());

        assertEquals(session, sessionFromDatabase);
        assertEquals(true, SessionDatabase.existsUserSession(userId));
    }

    @Test
    @DisplayName("만료된 세션들을 정상적으로 반환되는지 테스트")
    void expiredSessionTest(){
        String userId1 = "userId";
        String userId2 = "user";

        Session session1 = SessionManager.createSession(userId1, -1);
        Session session2 = SessionManager.createSession(userId2, 30);

        SessionDatabase.addSession(session1);
        SessionDatabase.addSession(session2);

        List<Session> expiredSessions = SessionDatabase.findExpiredSession();

        assertEquals(1, expiredSessions.size());
        assertEquals(session1, expiredSessions.get(0));
    }

    @Test
    @DisplayName("세션들을 정상적으로 삭제하는지 테스트")
    void deleteSessionTest(){
        String userId = "userId";

        Session session = SessionManager.createSession(userId);

        SessionDatabase.addSession(session);
        SessionDatabase.deleteSession(session.getId());

        assertEquals(false, SessionDatabase.existsUserSession(userId));
    }
}