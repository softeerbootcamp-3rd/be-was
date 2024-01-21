package controller;

import data.RequestData;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

public class RequestDataControllerTest {

    static Stream<Object[]> provideResourcePaths() {
        return Stream.of(
                new Object[]{"/index.html", "200 /index.html"},
                new Object[]{"/user/form.html", "200 /user/form.html"},
                new Object[]{"/user/list.html", "200 /user/list.html"},
                new Object[]{"/user/login.html", "200 /user/login.html"},
                new Object[]{"/user/profile.html", "200 /user/profile.html"}
        );
    }

    static Stream<Object[]> provideInvalidResourcePaths() {
        return Stream.of(
                new Object[]{"/inddex.html", "404 /error/notfound.html"},
                new Object[]{"/user/foorm.html", "404 /error/notfound.html"},
                new Object[]{"/user/lisst.html", "404 /error/notfound.html"},
                new Object[]{"/user/logiin.html", "404 /error/notfound.html"},
                new Object[]{"/user/profiile.html", "404 /error/notfound.html"}
        );
    }

    static Stream<Object[]> provideApiPaths() {
        return Stream.of(
                new Object[]{"/", "302 /index.html"},
                new Object[]{"/user/create?userId=asdf&password=asdf&name=asdf&email=asdf", "302 /index.html"}
        );
    }

    static Stream<Object[]> provideInvalidSignup() {
        return Stream.of(
                new Object[]{"/user/create?userId=&password=asdf&name=asdf&email=asdf", "400 /error/badrequest.html"},
                new Object[]{"/user/create?userId=asdf&password=&name=asdf&email=asdf", "400 /error/badrequest.html"},
                new Object[]{"/user/create?userId=asdf&password=asdf&name=&email=asdf", "400 /error/badrequest.html"},
                new Object[]{"/user/create?userId=asdf&password=asdf&name=asdf&email=", "400 /error/badrequest.html"}
        );
    }

    @ParameterizedTest
    @MethodSource("provideResourcePaths")
    @DisplayName("리소스 라우팅 테스트")
    public void routeRequest_Resource(String path, String expected) throws IOException {
        // Given
        Map<String, String> map = new HashMap<>();

        // When
        String actual = RequestDataController.routeRequest(new RequestData("GET", path, "HTTP/1.1", map));

        // Then
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidResourcePaths")
    @DisplayName("유효하지않은 리소스 라우팅 테스트")
    public void routeRequest_InvalidResource(String path, String expected) throws IOException {
        // Given
        Map<String, String> map = new HashMap<>();

        // When
        String actual = RequestDataController.routeRequest(new RequestData("GET", path, "HTTP/1.1", map));

        // Then
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideApiPaths")
    @DisplayName("API 라우팅 테스트")
    public void routeRequest_Api(String path, String expected) throws IOException {
        // Given
        Map<String, String> map = new HashMap<>();

        // When
        String actual = RequestDataController.routeRequest(new RequestData("GET", path, "HTTP/1.1", map));

        // Then
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidSignup")
    @DisplayName("회원가입 부족한 파라미터 테스트")
    public void routeRequest_InvalidSignup(String path, String expected) throws IOException {
        // Given
        Map<String, String> map = new HashMap<>();

        // When
        String actual = RequestDataController.routeRequest(new RequestData("GET", path, "HTTP/1.1", map));

        // Then
        assertThat(actual).isEqualTo(expected);
    }
}
