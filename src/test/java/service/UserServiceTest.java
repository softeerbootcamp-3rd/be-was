package service;

import com.google.common.collect.Maps;
import db.Database;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static common.config.WebServerConfig.userService;
import static org.assertj.core.api.Assertions.assertThat;

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
}