package service;

import db.Database;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import request.user.LoginRequest;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.stream.Stream;

import static db.Database.addUser;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("UserServiceTest 클래스")
class UserServiceTest {
    private UserService userService;
    private Database database;

    @BeforeEach
    void init() throws Exception {
        this.userService = UserService.getInstance();
        this.database = new Database();

        Constructor<User> constructor = User.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        User firstUser = constructor.newInstance();
        User secondUser = constructor.newInstance();

        Field[] fields = User.class.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            field.set(firstUser, "test1");
        }
        addUser(firstUser);

        for (Field field : fields) {
            field.setAccessible(true);
            field.set(secondUser, "test2");
        }
        addUser(secondUser);
    }

    @Test
    @DisplayName("회원가입이 정상적으로 처리되는지 확인")
    void sign_up_success() throws Exception {
        // given
        Constructor<User> constructor = User.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        User user = constructor.newInstance();

        Field[] fields = User.class.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            field.set(user, "test");
        }
        addUser(user);

        // when
        userService.join(user);
        User findUser = database.findUserById("test");

        // then
        assertThat(findUser).isNotNull();
    }

    @ParameterizedTest
    @MethodSource("login_success_parameters")
    @DisplayName("database에 회원정보가 존재하고 아이디와 비밀번호가 일치하면 true를 반환하는지 확인")
    void login_success(LoginRequest loginRequest) {

        // when
        boolean isLogin = userService.login(loginRequest);

        // then
        assertThat(isLogin).isTrue();
    }

    @ParameterizedTest
    @MethodSource("login_fail_parameters")
    @DisplayName("database에 회원정보가 존재하지 않는 경우나 비밀번호가 일치하지 않으면 false를 반환하는지 확인")
    void login_fail(LoginRequest loginRequest) {

        // when
        boolean isLogin = userService.login(loginRequest);

        // then
        assertThat(isLogin).isFalse();
    }

    private static Stream<Arguments> login_success_parameters() {
        return Stream.of(
                Arguments.of(new LoginRequest("test1", "test1")),
                Arguments.of(new LoginRequest("test2", "test2"))
        );
    }

    private static Stream<Arguments> login_fail_parameters() {
        return Stream.of(
                Arguments.of(new LoginRequest("test1", "test2")), // 비밀번호가 일치하지 않는 경우
                Arguments.of(new LoginRequest("test2", "test1")), // 비밀번호가 일치하지 않는 경우
                Arguments.of(new LoginRequest("test3", "test3")) // 회원정보가 존재하지 않는 경우
        );
    }
}
