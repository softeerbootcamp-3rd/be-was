package service;

import db.Database;
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
        User user = Database.findUserById("TestUserId");
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
    @DisplayName("loginUser(): 올바른 파라미터를 전달한 경우 로그인에 성공하고 세션 아이디를 반환한다")
    public void loginUserTest() {
        // given
        User testUser = new User("testUserId", "testPassword", "testName", "test@example.com");
        Database.addUser(testUser);

        Map<String, String> loginUserParams = new HashMap<>();
        loginUserParams.put("userId", testUser.getUserId());
        loginUserParams.put("password", testUser.getPassword());

        // when
        String sessionId = userService.loginUser(loginUserParams);

        // then
        Assertions.assertThat(sessionId).isNotNull();
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
        Database.addUser(testUser);

        Map<String, String> loginUserParams = new HashMap<>();
        loginUserParams.put("userId", testUser.getUserId());
        loginUserParams.put("password", "wrong_password");

        // when & then
        Assertions.assertThatThrownBy(() -> userService.loginUser(loginUserParams))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid userId and password");

    }
}
