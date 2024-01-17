package service;

import db.Database;
import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static config.WebServerConfig.userService;
import static org.assertj.core.api.Assertions.assertThat;

class UserServiceTest {

    @Test
    @DisplayName("회원가입 성공 테스트")
    void create() {

        //given
        User user = new User("javajigi", "password", "박재성", "javajigi@slipp.net");

        //when
        String userId = userService.create(user);

        //then
        assertThat(Database.findUserById(userId).getEmail()).isEqualTo("javajigi@slipp.net");
    }
}