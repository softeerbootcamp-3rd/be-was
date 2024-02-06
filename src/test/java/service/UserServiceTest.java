package service;

import com.google.common.collect.Maps;
import common.exception.DuplicateUserIdException;
import db.Database;
import dto.LoginRequest;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static common.Binder.*;
import static common.WebServerConfig.userService;
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

    @Test
    @DisplayName("로그인 성공 테스트")
    void loginSuccessTest() throws Exception {

        //given
        User user = new User("javajigi", "password", "박재성", "javajigi@slipp.net");
        Database.addUser(user);
        LoginRequest loginRequest = bindQueryStringToObject("userId=javajigi&password=password", LoginRequest.class);

        //when
        User result = userService.login(loginRequest);

        //then
        assertThat(result.getUserId()).isEqualTo("javajigi");
        assertThat(result.getName()).isEqualTo("박재성");
        assertThat(result.getEmail()).isEqualTo("javajigi@slipp.net");
    }

    @Test
    @DisplayName("로그인 실패 테스트 - 회원가입하지 않은 사용자가 로그인하는 경우 NoSuchElementException 예외 발생")
    void loginFailTest1() throws Exception {

        //given
        LoginRequest loginRequest = bindQueryStringToObject("userId=javajigi&password=password", LoginRequest.class);

        //when
        User result = userService.login(loginRequest);

        //then
        assertThat(result).isEqualTo(null);
    }

    @Test
    @DisplayName("로그인 실패 테스트 - 비밀번호가 틀린 경우 LoginFailException 예외 발생")
    void loginFailTest2() throws Exception {

        //given
        User user = new User("javajigi", "password", "박재성", "javajigi@slipp.net");
        Database.addUser(user);
        LoginRequest loginRequest = bindQueryStringToObject("userId=javajigi&password=xxxxx", LoginRequest.class);

        //when
        User result = userService.login(loginRequest);

        //then
        assertThat(result).isEqualTo(null);
    }
}
