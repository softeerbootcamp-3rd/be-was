package service;

import com.google.common.collect.Maps;
import common.exception.DuplicateUserIdException;
import db.Database;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static common.config.WebServerConfig.userService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserServiceTest {

    @AfterEach
    void tearDown() throws NoSuchFieldException, IllegalAccessException {
        Class<Database> databaseClass = Database.class;
        Field userDatabase = databaseClass.getDeclaredField("users");
        userDatabase.setAccessible(true);
        userDatabase.set(databaseClass, Maps.newHashMap());
    }

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

    @Test
    @DisplayName("중복 회원가입 예외처리 테스트 - 중복된 사용자 아이디가 존재하면 예외를 던진다.")
    void duplicateUserCreate() {

        //given
        User user1 = new User("javajigi", "password1", "박재성1", "javajigi@slipp.net1");
        User user2 = new User("javajigi", "password2", "박재성2", "javajigi@slipp.net2");

        //when, then
        userService.create(user1);
        assertThatThrownBy(() -> userService.create(user2))
                .isInstanceOf(DuplicateUserIdException.class);
    }
}
