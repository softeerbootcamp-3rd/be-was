package util;

import constant.HttpHeader;
import db.SessionDatabase;
import db.UserDatabase;
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

        Map<HttpHeader, String> cookieHeader = Map.of(HttpHeader.COOKIE, "sid=" + testSessionId);

        // when
        User loggedInUser = SessionUtil.getUserByCookie(cookieHeader);

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
//        HttpRequestDto requestNoCookie = new HttpRequestDtoBuilder("GET", "/index.html", "HTTP/1.1").build();
        Map<HttpHeader, String> requestNoCookie = null;

        // 2. 쿠키 헤더 값에 sid가 없는 request
//        HttpRequestDto requestNoSid = new HttpRequestDtoBuilder("GET", "/index.html", "HTTP/1.1")
//                .setHeaders(new HttpHeaders(Map.of(HttpHeader.COOKIE, "cookie=hello"))).build();
        Map<HttpHeader, String> requestNoSid = Map.of(HttpHeader.COOKIE, "cookie=hello");

        // 3. sid에 해당하는 세션이 없는 request
//        HttpRequestDto requestEmptySid = new HttpRequestDtoBuilder("GET", "/index.html", "HTTP/1.1")
//                .setHeaders(new HttpHeaders(Map.of(HttpHeader.COOKIE, "sid=1234"))).build();
        Map<HttpHeader, String> requestEmptySid = Map.of(HttpHeader.COOKIE, "sid=1234");

        // 4. sid에 해당하는 세션의 expireDate가 지나 유효하지 않은 request
        Session expiredSession = new Session("testUser");
        Class<Session> clazz = Session.class;
        Field expireDate = clazz.getDeclaredField("expireDate");
        expireDate.setAccessible(true);
        expireDate.set(expiredSession, LocalDateTime.now().minusDays(1));
        String expiredSessionId = SessionDatabase.addSession(expiredSession);

//        HttpRequestDto requestExpiredSid = new HttpRequestDtoBuilder("GET", "/index.html", "HTTP/1.1")
//                .setHeaders(new HttpHeaders(Map.of(HttpHeader.COOKIE, "sid=" + expiredSessionId))).build();

        Map<HttpHeader, String> requestExpiredSid = Map.of(HttpHeader.COOKIE, "sid=" + expiredSessionId);

        // 5. sid에 해당하는 세션에 매핑되는 user 정보가 없는 request
        String noUserSid = SessionDatabase.addSession(new Session("noUser"));
//        HttpRequestDto requestNoUserSid = new HttpRequestDtoBuilder("GET", "/index.html", "HTTP/1.1")
//                .setHeaders(new HttpHeaders(Map.of(HttpHeader.COOKIE, "sid=" + noUserSid))).build();
        Map<HttpHeader, String> requestNoUserSid = Map.of(HttpHeader.COOKIE, "sid=" + noUserSid);

        // when & then
        Assertions.assertThat(SessionUtil.getUserByCookie(requestNoCookie)).isNull();
        System.out.println(1);
        Assertions.assertThat(SessionUtil.getUserByCookie(requestNoSid)).isNull();
        System.out.println(2);
        Assertions.assertThat(SessionUtil.getUserByCookie(requestEmptySid)).isNull();
        System.out.println(3);
        Assertions.assertThat(SessionUtil.getUserByCookie(requestExpiredSid)).isNull();
        System.out.println(4);
        Assertions.assertThat(SessionUtil.getUserByCookie(requestNoUserSid)).isNull();
    }
}
