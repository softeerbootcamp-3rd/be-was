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
    @DisplayName("회원가입 정상처리")
    void saveUser() {
        // given
        Map<String, String> userParams = Map.of("userId", "user", "password", "pwd", "name", "user",
                "email", "user@email");

        // when
        userService.saveUser(userParams);

        // then
        User result = Database.findUserById(userParams.get("userId"));
        assertThat(result.getUserId()).as("사용자 아이디 일치").isEqualTo(userParams.get("userId"));
        assertThat(result.getPassword()).as("패스워드 일치").isEqualTo(userParams.get("password"));
        assertThat(result.getName()).as("이름 일치").isEqualTo(userParams.get("name"));
        assertThat(result.getEmail()).as("이메일 일치").isEqualTo(userParams.get("email"));
    }

    @Test
    @DisplayName("중복 회원가입")
    void saveUserDuplication() {
        // given
        Map<String, String> userParams1 = Map.of("userId", "user2", "password", "pwd", "name", "user",
                "email", "user@email");
        Map<String, String> userParams2 = Map.of("userId", "user2", "password", "pwd", "name", "user",
                "email", "user@email");
        userService.saveUser(userParams1);

        // when
        Throwable result = catchThrowable(() -> userService.saveUser(userParams2));

        // then
        assertThat(result).as("같은 아이디로 회원가입하는 경우 오류 발생")
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("회원가입 파라미터 부족")
    void saveUserNullParam() {
        // given
        Map<String, String> userParams1 = Map.of("password", "pwd", "name", "user", "email",
                "user@email");
        Map<String, String> userParams2 = Map.of("userId", "user", "name", "user", "email", "user@email");
        Map<String, String> userParams3 = Map.of("userId", "user", "password", "pwd", "email",
                "user@email");
        Map<String, String> userParams4 = Map.of();

        // when
        Throwable result1 = catchThrowable(() -> userService.saveUser(userParams1));
        Throwable result2 = catchThrowable(() -> userService.saveUser(userParams2));
        Throwable result3 = catchThrowable(() -> userService.saveUser(userParams3));
        Throwable result4 = catchThrowable(() -> userService.saveUser(userParams4));

        // then
        assertThat(result1).as("아이디가 비어있는 경우 오류 발생").isInstanceOf(NullPointerException.class);
        assertThat(result2).as("패스워드가 비어있는 경우 오류 발생").isInstanceOf(NullPointerException.class);
        assertThat(result3).as("이름이 비어있는 경우 오류 발생").isInstanceOf(NullPointerException.class);
        assertThat(result4).as("모두 비어있는 경우 오류 발생").isInstanceOf(NullPointerException.class);
    }
}