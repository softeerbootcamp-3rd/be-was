package domain.user.infrastructure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import common.db.Database;
import domain.user.command.domain.User;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("UserRepositoryImpl 클래스")
class UserRepositoryImplTest {

    private UserRepositoryImpl userRepository = new UserRepositoryImpl();
    private SessionStorageService sessionStorageService = new SessionStorageService();

    @BeforeEach
    void setUp() {
        // Database 초기화
        Database.Users().clear();
    }

    @AfterEach
    void tearDown() {
        // Database 상태 복원
        Database.Users().clear();
        Database.SessionStorage().clear();
    }

    @Test
    @DisplayName("유저 추가 성공 테스트")
    void addUser_Success() {
        // given
        User user = new User("1", "password", "name", "email");

        // when
        userRepository.addUser(user);

        // then
        assertNotNull(Database.Users().get("1"));
        assertEquals(user, Database.Users().get("1"));
    }

    @Test
    @DisplayName("유저 아이디로 유저 찾기 테스트")
    void findUserById_UserExists() {
        // given
        User user = new User("1", "password", "name", "email");
        Database.Users().put("1", user);

        // when
        Optional<User> foundUser = userRepository.findUserById("1");

        // then
        assertTrue(foundUser.isPresent());
        assertEquals(user, foundUser.get());
    }

    @Test
    @DisplayName("유저 아이디로 유저 찾기 실패 테스트")
    void findUserById_UserNotExists() {
        // given

        // when
        Optional<User> foundUser = userRepository.findUserById("nonexistent");

        // then
        assertFalse(foundUser.isPresent());
    }

    @Test
    @DisplayName("세션 아이디 저장 성공 테스트")
    void saveSessionAndGetSessionIdByUserId() {
        // given

        // when
        sessionStorageService.saveSession("sessionId", "1");

        // then
        assertEquals("1", Database.SessionStorage().get("sessionId"));
        assertEquals(Optional.of("sessionId"), sessionStorageService.getSessionIdByUserId("1"));
    }

    @Test
    @DisplayName("유저 아이디로 세션 아이디 찾기 실패 테스트")
    void getSessionIdByUserId_SessionNotExists() {
        assertEquals(Optional.empty(), sessionStorageService.getSessionIdByUserId("nonexistent"));
    }

    @Test
    @DisplayName("세션 아이디 삭제 성공 테스트")
    void removeSession_SessionExists() {
        // given
        Database.SessionStorage().put("sessionId", "1");

        // when
        sessionStorageService.removeSession("sessionId");

        // then
        assertNull(Database.SessionStorage().get("sessionId"));
    }



}
