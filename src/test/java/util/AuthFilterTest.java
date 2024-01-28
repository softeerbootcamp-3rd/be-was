package util;

import db.Session;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import request.http.HttpRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.stream.Stream;

import static db.Database.addUser;
import static db.Session.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AuthFilterTest {

    private Session session;

    @BeforeEach
    void init() throws Exception {
        this.session = new Session();

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

        addSession("1234", "test1");
        addSession("abcdefghijk", "test2");
    }

    @ParameterizedTest
    @MethodSource("login_user_parameters")
    @DisplayName("로그인한 유저가 로그인이 필요한 페이지에 접근하면 true를 반환하는지 확인")
    void login_user_test(HttpRequest httpRequest) {
        // when
        boolean isLogin = AuthFilter.isLogin(httpRequest);

        // then
        assertThat(isLogin).isTrue();
    }

    @ParameterizedTest
    @MethodSource("not_login_user_parameters")
    @DisplayName("로그인하지 않은 유저가 로그인이 필요한 페이지에 접근하면 false를 반환하는지 확인")
    void not_login_user_test(HttpRequest httpRequest)  {
        // when
        boolean isLogin = AuthFilter.isLogin(httpRequest);

        // then
        assertThat(isLogin).isFalse();
    }

    private static Stream<Arguments> login_user_parameters() throws IOException {
        return Stream.of(
                Arguments.of(new HttpRequest(new BufferedReader(new StringReader("GET /user/list HTTP/1.1"
                        + "\r\n"
                        + "Content-Type: application/x-www-form-urlencoded"
                        + "\r\n"
                        + "Cookie: sid=1234"
                        + "\r\n"
                        + "\r\n")))),
                Arguments.of(new HttpRequest(new BufferedReader(new StringReader("GET /user/list HTTP/1.1"
                        + "\r\n"
                        + "Content-Type: application/x-www-form-urlencoded"
                        + "\r\n"
                        + "Cookie: sid=abcdefghijk"
                        + "\r\n"
                        + "\r\n"))))
        );
    }

    private static Stream<Arguments> not_login_user_parameters() throws IOException {
        return Stream.of(
                Arguments.of(new HttpRequest(new BufferedReader(new StringReader("GET /user/list HTTP/1.1"
                        + "\r\n"
                        + "Content-Type: application/x-www-form-urlencoded"
                        + "\r\n"
                        + "\r\n")))),
                Arguments.of(new HttpRequest(new BufferedReader(new StringReader("GET /user/list HTTP/1.1"
                        + "\r\n"
                        + "Content-Type: application/x-www-form-urlencoded"
                        + "\r\n"
                        + "Accept: */*"
                        + "\r\n"
                        + "\r\n"))))
        );
    }
}