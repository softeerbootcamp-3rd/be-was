package Service;

import controller.HttpMethod;
import data.CookieData;
import data.RequestData;
import db.Database;
import db.Session;
import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import service.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.*;

public class UserServiceTest {

    private static Stream<Arguments> provideValidUserData() {
        return Stream.of(
                Arguments.of("testUser1", "testPass1", "John", "john@example.com"),
                Arguments.of("testUser2", "testPass2", "Jane", "jane@example.com"),
                Arguments.of("testUser3", "testPass3", "Doe", "doe@example.com")
        );
    }

    @ParameterizedTest
    @MethodSource("provideValidUserData")
    void registerUser_ValidData(String userId, String password, String name, String email) {
        // Given
        Map<String, String> map = new HashMap<>();
        String query = "userId=" + userId + "&password=" + password + "&name=" + name + "&email=" + email;
        RequestData requestData = new RequestData(HttpMethod.POST, "/user/create", "HTTP/1.1", map, query, false);

        // When
        UserService.registerUser(requestData);

        // Then
        User addedUser = Database.findUserById(userId);
        assertThat(addedUser).isNotNull();
        assertThat(addedUser.getUserId()).isEqualTo(userId);
        assertThat(addedUser.getPassword()).isEqualTo(password);
        assertThat(addedUser.getName()).isEqualTo(name);
        assertThat(addedUser.getEmail()).isEqualTo(email);
    }

    private static Stream<Arguments> provideInvalidUserData() {
        return Stream.of(
                Arguments.of("testUser1", "testPass1", "John", ""),
                Arguments.of("testUser2", "testPass2", "Jane", ""),
                Arguments.of("testUser3", "testPass3", "", "")
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidUserData")
    void registerUser_InvalidData(String userId, String password, String name, String email) {
        // Given
        Map<String, String> map = new HashMap<>();
        String query = "userId=" + userId + "&password=" + password + "&name=" + name + "&email=" + email;
        RequestData requestData = new RequestData(HttpMethod.GET, "/user/create", "HTTP/1.1", map, query, false);

        // When, Then
        assertThrows(Exception.class, () -> UserService.registerUser(requestData));
    }

    @Test
    @DisplayName("유효한 로그인 테스트")
    public void login_ValidUser_ReturnsCookieData() {
        // Given
        // 사용자 저장
        User testUser = new User("test", "test", "Test User", "test@example.com");
        Database.addUser(testUser);
        // 로그인 요청 생성
        RequestData requestData = new RequestData(HttpMethod.POST, "/user/login", "HTTP/1.1", new HashMap<>(), "userId=test&password=test", false);

        // When
        CookieData actual = UserService.login(requestData);

        // Then
        assertThat(actual).isNotNull();
        assertThat(actual.getMaxAge()).isEqualTo(60 * 5);
    }

    @Test
    @DisplayName("유효하지 않은 로그인 테스트")
    public void login_InvalidUser_ReturnsNull() {
        // Given
        User testUser = new User("test", "test", "Test User", "test@example.com");
        Database.addUser(testUser);
        RequestData requestData = new RequestData(HttpMethod.POST, "/user/login", "HTTP/1.1", new HashMap<>(), "userId=test&password=wrongpassword", false);

        // When
        CookieData actual = UserService.login(requestData);

        // Then
        assertThat(actual).isNull();
    }

    @Test
    @DisplayName("로그아웃 테스트")
    public void logout_ValidSession_ReturnsExpiredCookieData() {
        // Given
        User testUser = new User("test", "test", "Test User", "test@example.com");
        Database.addUser(testUser);
        String sessionId = Session.createSession(testUser.getUserId());
        RequestData requestData = new RequestData(HttpMethod.GET, "/logout", "HTTP/1.1", new HashMap<>(), true);
        requestData.getHeaders().put("Cookie", "sid=" + sessionId);

        // When
        CookieData actual = UserService.logout(requestData);

        // Then
        assertThat(actual.getSid()).isEqualTo(sessionId);
        assertThat(actual.getMaxAge()).isEqualTo(0);
    }

    @Test
    @DisplayName("로그아웃 시 세션이 존재하지 않는 경우")
    public void logout_NoSession_ReturnsNull() {
        // Given
        RequestData requestData = new RequestData(HttpMethod.GET, "/logout", "HTTP/1.1", new HashMap<>(), true);

        // When
        CookieData actual = UserService.logout(requestData);

        // Then
        assertThat(actual).isNull();
    }
}
