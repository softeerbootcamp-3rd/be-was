package service;

import static org.assertj.core.api.Assertions.*;

import db.Database;
import java.util.Map;
import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserServiceTest {

    private final UserService userService = new UserService();

    @Test
    @DisplayName("회원가입")
    void saveUser() {
        // given
        Map<String, String> case1 = Map.of("userId", "user", "password", "pwd", "name", "user",
                "email", "user@email");
        Map<String, String> case2 = Map.of("userId", "user", "password", "pwd", "name", "user",
                "email", "user@email");

        // when
        userService.saveUser(case1);
        User result1 = Database.findUserById(case1.get("userId"));

        Throwable result2 = catchThrowable(() -> userService.saveUser(case2));

        // then
        assertThat(result1.getUserId()).as("사용자 아이디 일치").isEqualTo(case1.get("userId"));
        assertThat(result1.getPassword()).as("패스워드 일치").isEqualTo(case1.get("password"));
        assertThat(result1.getName()).as("이름 일치").isEqualTo(case1.get("name"));
        assertThat(result1.getEmail()).as("이메일 일치").isEqualTo(case1.get("email"));
        assertThat(result2).as("같은 아이디로 회원가입하는 경우 ").isInstanceOf(IllegalArgumentException.class);
    }
}