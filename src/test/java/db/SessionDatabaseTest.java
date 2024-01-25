package db;

import model.Session;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

public class SessionDatabaseTest {
    @Test
    @DisplayName("addSession(), findSessionById(): 세션 정보가 DB에 추가되고 추가된 정보를 sessionId로 읽어오는 기능 테스트")
    public void addSessionTest() {
        // given
        Session testSession = new Session("testUserId");

        // when
        String addedSessionId = SessionDatabase.addSession(testSession);
        Session addedSession = SessionDatabase.findSessionById(addedSessionId);

        // then
        Assertions.assertThat(addedSession.getSessionId()).isEqualTo(testSession.getSessionId());
        Assertions.assertThat(addedSession.getUserId()).isEqualTo(testSession.getUserId());
        Assertions.assertThat(addedSession.getExpireDate()).isEqualTo(testSession.getExpireDate());
        Assertions.assertThat(addedSession.getCreateDate()).isEqualTo(testSession.getCreateDate());
    }

    @Test
    @DisplayName("findValidSessionByUserId(): userId 값으로 isValid=true 인 세션 정보를 읽어오는 기능 테스트")
    public void findValidSessionByUserIdTest() throws NoSuchFieldException, IllegalAccessException {
        // given
        Session invalidSession = new Session("testUserId");
        Class<Session> clazz = Session.class;
        Field isValid = clazz.getDeclaredField("isValid");
        isValid.setAccessible(true);
        isValid.set(invalidSession, false);
        SessionDatabase.addSession(invalidSession);

        Session validSession = new Session("testUserId");
        SessionDatabase.addSession(validSession);

        // when
        Session addedSession = SessionDatabase.findValidSessionByUserId(validSession.getUserId());

        // then
        Assertions.assertThat(addedSession).isNotNull();
        Assertions.assertThat(addedSession.getUserId()).isEqualTo(validSession.getUserId());
        Assertions.assertThat(addedSession.getIsValid()).isEqualTo(true);
        Assertions.assertThat(addedSession.getSessionId()).isEqualTo(validSession.getSessionId());
    }
}
