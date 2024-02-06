package service;

import model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserServiceTest {
    static final UserService userService = UserService.getInstance();

    @BeforeAll
    static void init() {
        userService.saveUser("jinseop", "jinseop", "jinseop", "jinseop@abc.com");
    }

    @Test
    @DisplayName("유저 저장 성공 및 실패 테스트")
    void saveUserTest() {
        assertThat(
                userService.saveUser("hong", "hong", "hong", "hong@hong.com")
        ).isEqualTo(true);

        assertThat(
                userService.saveUser("jinseop", "jinseop", "jinseop", "jinseop@abc.com")
        ).isEqualTo(false);
    }

    @Test
    @DisplayName("유저 정보를 가져오는 성공 및 실패 테스트")
    void getUserTest() {
        User invalidPasswordUser = userService.getUser("jinseop", "123");
        User validUser = userService.getUser("jinseop", "jinseop");
        User notExistUser = userService.getUser("jin", "seop");

        assertThat(invalidPasswordUser).isNull();
        assertThat(notExistUser).isNull();
        assertThat(validUser)
                .hasFieldOrPropertyWithValue("userId", "jinseop")
                .hasFieldOrPropertyWithValue("name", "jinseop");
    }
}
