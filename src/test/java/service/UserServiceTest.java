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

    @Test()
    @DisplayName("UserService.createUser() test")
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

    @Test()
    @DisplayName("UserService.createUser() Fail Case: IllegalArgumentException")
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
}
