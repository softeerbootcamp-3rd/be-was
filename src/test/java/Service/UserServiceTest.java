package Service;

import data.RequestData;
import db.Database;
import model.User;
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
        RequestData requestData = new RequestData("GET", "/user/create?" + query, "HTTP/1.1", map);

        // When
        UserService.registerUser(requestData);

        // Then
        User addedUser = Database.findUserById(userId);
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
        RequestData requestData = new RequestData("GET", "/user/create?" + query, "HTTP/1.1", map);

        // When, Then
        assertThrows(Exception.class, () -> UserService.registerUser(requestData));
    }
}
