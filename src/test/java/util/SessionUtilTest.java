package util;

import db.SessionDatabase;
import db.UserDatabase;
import dto.HttpRequestDto;
import dto.HttpRequestDtoBuilder;
import model.Session;
import model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Map;

public class SessionUtilTest {
    @Test
    @DisplayName("getUserByCookieTest(): 유저가 유효한 sid 쿠키 값을 가지고 있는 경우 해당하는 유저 객체를 반환한다")
    public void getUserByCookieTest() {
        // given
        User testUser = new User("testUserId", "testPW", "testName", "test@exmaple.com");
        UserDatabase.addUser(testUser);

        String testSessionId = SessionDatabase.addSession(new Session(testUser.getUserId()));

        Map<String, String> cookieHeader = Map.of("Cookie", "sid=" + testSessionId);
        HttpRequestDto request = new HttpRequestDtoBuilder("GET", "/index.html", "HTTP/1.1")
                .setHeaders(cookieHeader).build();

        // when
        User loggedInUser = SessionUtil.getUserByCookie(request.getHeaders());

        // then
        Assertions.assertThat(loggedInUser.getUserId()).isEqualTo(testUser.getUserId());
        Assertions.assertThat(loggedInUser.getPassword()).isEqualTo(testUser.getPassword());
        Assertions.assertThat(loggedInUser.getName()).isEqualTo(testUser.getName());
        Assertions.assertThat(loggedInUser.getEmail()).isEqualTo(testUser.getEmail());
    }

    @Test
    @DisplayName("getUserByCookieTest(): 유저가 유효한 sid 쿠키 값을 가지고 있지 않은 경우 null을 반환한다")
    public void getUserByCookieFailTest() throws NoSuchFieldException, IllegalAccessException {
        // given
        // 1. 쿠키 헤더가 없는 request
        HttpRequestDto requestNoCookie = new HttpRequestDtoBuilder("GET", "/index.html", "HTTP/1.1").build();

        // 2. 쿠키 헤더 값에 sid가 없는 request
        HttpRequestDto requestNoSid = new HttpRequestDtoBuilder("GET", "/index.html", "HTTP/1.1")
                .setHeaders(Map.of("Cookie", "cookie=hello")).build();

        // 3. sid에 해당하는 세션이 없는 request
        HttpRequestDto requestEmptySid = new HttpRequestDtoBuilder("GET", "/index.html", "HTTP/1.1")
                .setHeaders(Map.of("Cookie", "sid=1234")).build();

        // 4. sid에 해당하는 세션의 expireDate가 지나 유효하지 않은 request
        Session expiredSession = new Session("testUser");
        Class<Session> clazz = Session.class;
        Field expireDate = clazz.getDeclaredField("expireDate");
        expireDate.setAccessible(true);
        expireDate.set(expiredSession, LocalDateTime.now().minusDays(1));
        String expiredSessionId = SessionDatabase.addSession(expiredSession);

        HttpRequestDto requestExpiredSid = new HttpRequestDtoBuilder("GET", "/index.html", "HTTP/1.1")
                .setHeaders(Map.of("Cookie", "sid=" + expiredSessionId)).build();

        // 5. sid에 해당하는 세션에 매핑되는 user 정보가 없는 request
        String noUserSid = SessionDatabase.addSession(new Session("noUser"));
        HttpRequestDto requestNoUserSid = new HttpRequestDtoBuilder("GET", "/index.html", "HTTP/1.1")
                .setHeaders(Map.of("Cookie", "sid=" + noUserSid)).build();

        // when & then
        Assertions.assertThat(SessionUtil.getUserByCookie(requestNoCookie.getHeaders())).isNull();
        Assertions.assertThat(SessionUtil.getUserByCookie(requestNoSid.getHeaders())).isNull();
        Assertions.assertThat(SessionUtil.getUserByCookie(requestEmptySid.getHeaders())).isNull();
        Assertions.assertThat(SessionUtil.getUserByCookie(requestExpiredSid.getHeaders())).isNull();
        Assertions.assertThat(SessionUtil.getUserByCookie(requestNoUserSid.getHeaders())).isNull();
    }
}
