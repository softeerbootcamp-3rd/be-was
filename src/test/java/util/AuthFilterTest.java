package util;

import db.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import request.http.HttpRequest;

import java.io.*;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AuthFilterTest {

    private Session session;

    @BeforeEach
    void init() {
        this.session = new Session();
        Session.addSession("1234", "taegon1998");
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
        String httpRequestString = "GET /user/create HTTP/1.1\r\n"
                + "Content-Length: 27\r\n"
                + "Content-Type: application/x-www-form-urlencoded\r\n"
                + "Cookie: sid=1234; path=/\r\n"
                + "\r\n"
                + "userId=taegon1998&password=1234";
        InputStream inputStream = new ByteArrayInputStream(httpRequestString.getBytes("ISO-8859-1"));
        return Stream.of(
                Arguments.of(new HttpRequest(inputStream))
        );
    }

    private static Stream<Arguments> not_login_user_parameters() throws IOException {
          String httpRequestString = "GET /user/create HTTP/1.1\r\n"
                    + "Content-Length: 27\r\n"
                    + "Content-Type: application/x-www-form-urlencoded\r\n"
                    + "\r\n"
                    + "userId=test3&password=test3";
            InputStream inputStream = new ByteArrayInputStream(httpRequestString.getBytes("ISO-8859-1"));
            return Stream.of(
                    Arguments.of(new HttpRequest(inputStream))
            );
    }
}