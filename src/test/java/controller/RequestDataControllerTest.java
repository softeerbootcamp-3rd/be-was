package controller;

import data.RequestData;
import data.Response;
import db.Database;
import db.Session;
import model.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import service.UserService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

public class RequestDataControllerTest {

    static Stream<Object[]> provideResourcePaths() {
        return Stream.of(
                new Object[]{new RequestData(HttpMethod.GET, "/index.html", "HTTP/1.1", new HashMap<>(), false), new Response(HttpStatusCode.OK, "/index.html")},
                new Object[]{new RequestData(HttpMethod.GET, "/user/form.html", "HTTP/1.1", new HashMap<>(), false), new Response(HttpStatusCode.OK, "/user/form.html")},
                new Object[]{new RequestData(HttpMethod.GET, "/user/list.html", "HTTP/1.1", new HashMap<>(), false), new Response(HttpStatusCode.FOUND, "/index.html")},
                new Object[]{new RequestData(HttpMethod.GET, "/user/login.html", "HTTP/1.1", new HashMap<>(), false), new Response(HttpStatusCode.OK, "/user/login.html")},
                new Object[]{new RequestData(HttpMethod.GET, "/user/profile.html", "HTTP/1.1", new HashMap<>(), false), new Response(HttpStatusCode.OK, "/user/profile.html")}
        );
    }

    static Stream<Object[]> provideInvalidResourcePaths() {
        return Stream.of(
                new Object[]{new RequestData(HttpMethod.GET, "/inddex.html", "HTTP/1.1", new HashMap<>(), false), new Response(HttpStatusCode.NOT_FOUND, "/error/notfound.html")},
                new Object[]{new RequestData(HttpMethod.GET, "/foorm.html", "HTTP/1.1", new HashMap<>(), false), new Response(HttpStatusCode.NOT_FOUND, "/error/notfound.html")},
                new Object[]{new RequestData(HttpMethod.GET, "/lisst.html", "HTTP/1.1", new HashMap<>(), false), new Response(HttpStatusCode.NOT_FOUND, "/error/notfound.html")},
                new Object[]{new RequestData(HttpMethod.GET, "/logiin.html", "HTTP/1.1", new HashMap<>(), false), new Response(HttpStatusCode.NOT_FOUND, "/error/notfound.html")},
                new Object[]{new RequestData(HttpMethod.GET, "/profiile.html", "HTTP/1.1", new HashMap<>(), false), new Response(HttpStatusCode.NOT_FOUND, "/error/notfound.html")}
        );
    }

    static Stream<Object[]> provideApiPaths() {
        return Stream.of(
                new Object[]{new RequestData(HttpMethod.GET, "/", "HTTP/1.1", new HashMap<>(), false), new Response(HttpStatusCode.FOUND, "/index.html")},
                new Object[]{new RequestData(HttpMethod.POST, "/user/create", "HTTP/1.1", new HashMap<>(), "userId=asdf&password=asdf&name=asdf&email=asdf", false), new Response(HttpStatusCode.FOUND, "/index.html")}
        );
    }

    static Stream<Object[]> provideInvalidSignup() {
        return Stream.of(
                new Object[]{new RequestData(HttpMethod.POST, "/user/create", "HTTP/1.1", new HashMap<>(), "userId=&password=asdf&name=asdf&email=asdf", false), new Response(HttpStatusCode.BAD_REQUEST, "/error/badrequest.html")},
                new Object[]{new RequestData(HttpMethod.POST, "/user/create", "HTTP/1.1", new HashMap<>(), "userId=asdf&password=&name=asdf&email=asdf", false), new Response(HttpStatusCode.BAD_REQUEST, "/error/badrequest.html")},
                new Object[]{new RequestData(HttpMethod.POST, "/user/create", "HTTP/1.1", new HashMap<>(), "userId=asdf&password=asdf&name=&email=asdf", false), new Response(HttpStatusCode.BAD_REQUEST, "/error/badrequest.html")},
                new Object[]{new RequestData(HttpMethod.POST, "/user/create", "HTTP/1.1", new HashMap<>(), "userId=asdf&password=asdf&name=asdf&email=", false), new Response(HttpStatusCode.BAD_REQUEST, "/error/badrequest.html")}
        );
    }

    @ParameterizedTest
    @MethodSource("provideResourcePaths")
    @DisplayName("리소스 라우팅 테스트")
    public void routeRequest_Resource(RequestData requestData, Response expected) throws IOException {
        // When
        Response actual = RequestDataController.routeRequest(requestData);

        // Then
        assertThat(actual.getStatus()).isEqualTo(expected.getStatus());
        assertThat(actual.getPath()).isEqualTo(expected.getPath());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidResourcePaths")
    @DisplayName("유효하지않은 리소스 라우팅 테스트")
    public void routeRequest_InvalidResource(RequestData requestData, Response expected) throws IOException {
        // When
        Response actual = RequestDataController.routeRequest(requestData);

        // Then
        assertThat(actual.getStatus()).isEqualTo(expected.getStatus());
        assertThat(actual.getPath()).isEqualTo(expected.getPath());
    }

    @ParameterizedTest
    @MethodSource("provideApiPaths")
    @DisplayName("API 라우팅 테스트")
    public void routeRequest_Api(RequestData requestData, Response expected) throws IOException {
        // When
        Response actual = RequestDataController.routeRequest(requestData);

        // Then
        assertThat(actual.getStatus()).isEqualTo(expected.getStatus());
        assertThat(actual.getPath()).isEqualTo(expected.getPath());
        assertThat(actual.getCookie()).isEqualTo(expected.getCookie());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidSignup")
    @DisplayName("회원가입 부족한 파라미터 테스트")
    public void routeRequest_InvalidSignup(RequestData requestData, Response expected) throws IOException {
        // When
        Response actual = RequestDataController.routeRequest(requestData);

        // Then
        assertThat(actual.getStatus()).isEqualTo(expected.getStatus());
        assertThat(actual.getPath()).isEqualTo(expected.getPath());
    }

    @Test
    @DisplayName("기존 로그인 유저가 다시 로그인에 접근하면 홈으로 리다이렉션하는지 검증")
    public void testRedirectToHomeIfLoggedInUserAccessesLoginPage() {
        // Given
        User testUser = new User("test", "test", "Test User", "test@example.com");
        Database.addUser(testUser);
        String sessionId = Session.createSession(testUser.getUserId());

        Map<String, String> map = new HashMap<>();
        RequestData requestData = new RequestData(HttpMethod.GET, "/user/login.html", "HTTP/1.1", map, true);
        requestData.getHeaders().put("Cookie", "sid=" + sessionId);


        // When
        Response actual = RequestDataController.routeRequest(requestData);

        // Then
        assertThat(actual.getStatus()).isEqualTo(HttpStatusCode.FOUND);
        assertThat(actual.getPath()).isEqualTo("/index.html");
        assertThat(actual.getCookie()).isNull();
    }

    @Test
    @DisplayName("로그인되지 않은 유저가 로그인 페이지에 접근하면 로그인 페이지를 반환하는지 검증")
    public void testReturnLoginPageForNonLoggedInUser() {
        // Given
        Map<String, String> map = new HashMap<>();
        RequestData requestData = new RequestData(HttpMethod.GET, "/user/login.html", "HTTP/1.1", map, false);

        // When
        Response actual = RequestDataController.routeRequest(requestData);

        // Then
        assertThat(actual.getStatus()).isEqualTo(HttpStatusCode.OK);
        assertThat(actual.getPath()).isEqualTo("/user/login.html");
        assertThat(actual.getCookie()).isNull();
    }

    @Test
    @DisplayName("API 로그인 요청에 대한 응답 검증")
    public void testApiLoginResponse() {
        // Given
        Map<String, String> map = new HashMap<>();
        String userId = "testUser";
        String password = "testPass";
        String name = "Test User";
        String email = "test@example.com";
        RequestData signupRequest = new RequestData(HttpMethod.POST, "/user/create", "HTTP/1.1", map,
                "userId=" + userId + "&password=" + password + "&name=" + name + "&email=" + email, false);

        // SignUp: 사용자 추가
        RequestDataController.routeRequest(signupRequest);

        RequestData requestData = new RequestData(HttpMethod.POST, "/user/login", "HTTP/1.1", map,
                "userId=" + userId + "&password=" + password, false);

        // When
        Response actual = RequestDataController.routeRequest(requestData);

        // Then
        assertThat(actual.getStatus()).isEqualTo(HttpStatusCode.FOUND);
        assertThat(actual.getPath()).isEqualTo("/index.html");
        assertThat(actual.getCookie()).isNotNull();
    }

    @Test
    @DisplayName("API 로그아웃 요청에 대한 응답 검증")
    public void testApiLogoutResponse() {
        // Given
        User testUser = new User("test", "test", "Test User", "test@example.com");
        Database.addUser(testUser);
        String sessionId = Session.createSession(testUser.getUserId());

        Map<String, String> map = new HashMap<>();
        RequestData requestData = new RequestData(HttpMethod.GET, "/user/logout", "HTTP/1.1", map, true);
        requestData.getHeaders().put("Cookie", "sid=" + sessionId);

        // When
        Response actual = RequestDataController.routeRequest(requestData);

        // Then
        assertThat(actual.getStatus()).isEqualTo(HttpStatusCode.FOUND);
        assertThat(actual.getPath()).isEqualTo("/index.html");
        assertThat(actual.getCookie()).isNotNull();
    }

    @Test
    @DisplayName("파일 리스트 요청 테스트 - 로그인 상태")
    public void routeRequest_FileList_LoggedIn() {
        // Given
        RequestData requestData = new RequestData(HttpMethod.GET, "/user/list.html", "HTTP/1.1", new HashMap<>(), true);

        // When
        Response actual = RequestDataController.routeRequest(requestData);

        // Then
        assertThat(actual.getStatus()).isEqualTo(HttpStatusCode.OK);
        assertThat(actual.getPath()).isEqualTo("/user/list.html");
    }

    @Test
    @DisplayName("파일 리스트 요청 테스트 - 비로그인 상태")
    public void routeRequest_FileList_NotLoggedIn() {
        // Given
        RequestData requestData = new RequestData(HttpMethod.GET, "/user/list.html", "HTTP/1.1", new HashMap<>(), false);

        // When
        Response actual = RequestDataController.routeRequest(requestData);

        // Then
        assertThat(actual.getStatus()).isEqualTo(HttpStatusCode.FOUND);
        assertThat(actual.getPath()).isEqualTo("/index.html");
    }
}
