package service;

import db.Database;
import model.User;
import org.junit.jupiter.api.*;


import static org.assertj.core.api.Assertions.assertThat;

class UserLoginServiceTest {

    UserLoginService userLoginService = new UserLoginService();

    @AfterEach
    public void afterEach() {
        Database.clear();
    }

    @Test
    @DisplayName("정상 로그인 테스트")
    public void normalLoginTest() {
        User user = new User("id", "pass", "kim", "123@naver.com");
        Database.addUser(user);

        String userId = "id";
        String password = "pass";

        User loginUser = userLoginService.login(userId, password);
        assertThat(loginUser).isEqualTo(user);
    }

    @Test
    @DisplayName("Id가 등록되어 있지 않은 경우")
    public void notRegisteredId() {
        String userId = "id";
        String password = "pass";

        User loginUser = userLoginService.login(userId, password);
        assertThat(loginUser).isNull();
    }

    @Test
    @DisplayName("비밀번호가 틀린 경우")
    public void passwordNotMatch() {
        User user = new User("id", "pass", "kim", "123@naver.com");
        Database.addUser(user);

        String userId = "id";
        String password = "pass2";

        User loginUser = userLoginService.login(userId, password);
        assertThat(loginUser).isNull();
    }
}