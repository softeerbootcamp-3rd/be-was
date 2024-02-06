import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import model.User;
import http.session.SessionStorage;

import java.util.UUID;

public class SessionStorageTest {

    @Test
    public void testSessionStorage() {
        // 세션 저장소 인스턴스 생성
        SessionStorage sessionStorage = SessionStorage.getInstance();

        // 테스트 사용자 객체 생성
        User testUser = new User("testUser", "password", "Test User", "test@example.com");

        // 임의의 세션 ID 생성
        String sessionId = UUID.randomUUID().toString();

        // 세션 저장
        sessionStorage.addSession(sessionId, testUser);

        // 세션 조회
        User retrievedUser = sessionStorage.getUserBySessionId(sessionId);

        // 단언문: 조회된 사용자가 저장된 사용자와 동일한지 확인
        assertThat(retrievedUser).isEqualTo(testUser);
    }
}
