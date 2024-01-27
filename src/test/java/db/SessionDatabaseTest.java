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
}
