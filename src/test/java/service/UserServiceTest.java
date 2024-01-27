package service;

import db.SessionDatabase;
import db.UserDatabase;
import model.Session;
import model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserServiceTest {
    public static final UserService userService = new UserService();

    @Test
    @DisplayName("createUser(): 파라미터를 모두 잘 전달한 경우 User가 데이터베이스에 추가된다")
    public void createUserTest() {
        // given
        Map<String, String> createUserParams = new HashMap<>();
        createUserParams.put("userId", "TestUserId");
        createUserParams.put("password", "TestPassword");
        createUserParams.put("name", "TestUser");
        createUserParams.put("email", "test@example.com");

        // when
        userService.createUser(createUserParams);

        // then
        User user = UserDatabase.findUserById("TestUserId");
        Assertions.assertThat(user.getUserId()).isEqualTo(createUserParams.get("userId"));
        Assertions.assertThat(user.getPassword()).isEqualTo(createUserParams.get("password"));
        Assertions.assertThat(user.getEmail()).isEqualTo(createUserParams.get("email"));
        Assertions.assertThat(user.getName()).isEqualTo(createUserParams.get("name"));
    }

    @Test
    @DisplayName("createUser(): 파라미터를 모두 전달하지 않은 경우 IllegalArgumentException이 발생한다")
    public void createUserFailTest() {
        // given
        Map<String, String> createUserParams = new HashMap<>();
        createUserParams.put("userId", "TestUserId");
        createUserParams.put("password", "TestPassword");
        createUserParams.put("name", "TestUser");

        // when & then
        Assertions.assertThatThrownBy(() -> userService.createUser(createUserParams))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid Parameters");
    }

    @Test
    @DisplayName("createUser(): 중복된 userId를 입력한 경우 IllegalArgumentException이 발생한다.")
    public void createDuplicateUserTest() {
        // given
        UserDatabase.addUser(new User("TestUserId", "1234", "name", "email@example.com"));
        Map<String, String> createUserParams = new HashMap<>();
        createUserParams.put("userId", "TestUserId");
        createUserParams.put("password", "TestPassword");
        createUserParams.put("name", "TestUser");
        createUserParams.put("email", "email@example.com");

        // when & then
        Assertions.assertThatThrownBy(() -> userService.createUser(createUserParams))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Duplicate userId");
    }

    @Test
    @DisplayName("loginUser(): 유효한 기존 세션 정보가 없는 유저가 로그인에 성공한 경우 새로운 Session을 저장하고 해당 sessionId를 리턴한다")
    public void loginNewUserTest() {
        // given
        User testUser = new User("testUserId", "testPassword", "testName", "test@example.com");
        UserDatabase.addUser(testUser);
        Map<String, String> loginUserParams = new HashMap<>();
        loginUserParams.put("userId", testUser.getUserId());
        loginUserParams.put("password", testUser.getPassword());

        // when
        String sessionId = userService.loginUser(loginUserParams);

        // then
        Session session = SessionDatabase.findSessionById(sessionId);
        Assertions.assertThat(session.getUserId()).isEqualTo(testUser.getUserId());
    }

    @Test
    @DisplayName("loginUser(): 유효한 기존 세션 정보가 있는 유저가 로그인에 성공한 경우 기존에 존재하는 sessionId를 리턴한다")
    public void loginUserTest() {
        // given
        User testUser = new User("testUserId", "testPassword", "testName", "test@example.com");
        UserDatabase.addUser(testUser);
        String existedSessionId = SessionDatabase.addSession(new Session(testUser.getUserId()));

        Map<String, String> loginUserParams = new HashMap<>();
        loginUserParams.put("userId", testUser.getUserId());
        loginUserParams.put("password", testUser.getPassword());

        // when
        String sessionId = userService.loginUser(loginUserParams);

        // then
        Assertions.assertThat(sessionId).isEqualTo(existedSessionId);
    }

    @Test
    @DisplayName("loginUser(): 필요한 파라미터를 모두 전달하지 않은 경우 IllegalArgumentException이 발생한다")
    public void loginUserFailInvalidParamsTest() {
        // given
        Map<String, String> loginUserParams = new HashMap<>();
        loginUserParams.put("userId", "testUserId");

        // when & then
        Assertions.assertThatThrownBy(() -> userService.loginUser(loginUserParams))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid Parameters");
    }

    @Test
    @DisplayName("loginUser(): 잘못된 ID와 PW인 경우 IllegalArgumentException이 발생한다")
    public void loginUserFailInvalidIdPwTest() {
        // given
        User testUser = new User("testUserId", "testPassword", "testName", "test@example.com");
        UserDatabase.addUser(testUser);

        Map<String, String> loginUserParams = new HashMap<>();
        loginUserParams.put("userId", testUser.getUserId());
        loginUserParams.put("password", "wrong_password");

        // when & then
        Assertions.assertThatThrownBy(() -> userService.loginUser(loginUserParams))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid userId and password");

    }
}
