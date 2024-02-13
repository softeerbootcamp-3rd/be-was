package service;

import config.Config;
import controller.Controller;
import db.Database;
import dto.request.HTTPRequestDto;
import dto.response.HTTPResponseDto;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PostServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(PostServiceTest.class);

    @AfterEach
    void clearDB() {
        logger.debug("DB: {}", Database.findAllUser());
        Database.clearUsers();
    }

    // 회원가입 테스트 1: 바디에 key 값이 제대로 들어오지 않은 경우
    @Test
    void testSignUpBadKeys() {
        // given
        HTTPResponseDto expected = new HTTPResponseDto(400, "text/plain", "Bad Request".getBytes());
        HTTPRequestDto httpRequestDto = new HTTPRequestDto();
        httpRequestDto.setHTTPMethod("POST");
        httpRequestDto.setRequestTarget("/user/create");
        httpRequestDto.setHeader(new HashMap<>());
        Map<String, String> body = new HashMap<>();
        body.put("userid", "hello");                    // userid -> userId
        body.put("password", "1111");
        body.put("name", "world");
        body.put("email", "hello@gmail.com");
        httpRequestDto.setBody(body);
        // when
        HTTPResponseDto actual = Controller.doRequest(httpRequestDto);
        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    // 회원가입 테스트 2: 회원 정보를 다 입력하지 않았을 경우 (빈 문자열)
    @Test
    void testSignUpBadValues() {
        // given
        HTTPResponseDto expected = new HTTPResponseDto(400, "text/plain", "모든 정보를 기입해주세요.".getBytes());
        HTTPRequestDto httpRequestDto = new HTTPRequestDto();
        httpRequestDto.setHTTPMethod("POST");
        httpRequestDto.setRequestTarget("/user/create");
        httpRequestDto.setHeader(new HashMap<>());
        Map<String, String> body = new HashMap<>();
        body.put("userId", "hello");
        body.put("password", "1111");
        body.put("name", "world");
        body.put("email", "");
        httpRequestDto.setBody(body);
        // when
        HTTPResponseDto actual = Controller.doRequest(httpRequestDto);
        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    // 회원가입 테스트 3: 중복 회원이 존재할 경우
    @Test
    void testSignUpAlreadyExists() {
        // given
        HTTPResponseDto expected = new HTTPResponseDto(200, "text/plain", "이미 존재하는 아이디입니다. 다시 시도해주세요.".getBytes());
        // 첫 번째 유저 회원가입 처리
        HTTPRequestDto httpRequestDto = new HTTPRequestDto();
        httpRequestDto.setHTTPMethod("POST");
        httpRequestDto.setRequestTarget("/user/create");
        httpRequestDto.setHeader(new HashMap<>());
        Map<String, String> body = new HashMap<>();
        body.put("userId", "hello");
        body.put("password", "1111");
        body.put("name", "world");
        body.put("email", "hello@gmail.com");
        httpRequestDto.setBody(body);
        Controller.doRequest(httpRequestDto);
        // 두 번째 유저 회원가입 처리
        httpRequestDto = new HTTPRequestDto();
        httpRequestDto.setHTTPMethod("POST");
        httpRequestDto.setRequestTarget("/user/create");
        httpRequestDto.setHeader(new HashMap<>());
        body.put("userId", "hello");
        body.put("password", "2222");
        body.put("name", "world2");
        body.put("email", "hello2@gmail.com");
        httpRequestDto.setBody(body);
        // when
        HTTPResponseDto actual = Controller.doRequest(httpRequestDto);
        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    // 회원가입 테스트 4: 성공
    @Test
    void testSignUpSuccess() {
        // given
        HTTPResponseDto expected = new HTTPResponseDto("/index.html");
        HTTPRequestDto httpRequestDto = new HTTPRequestDto();
        httpRequestDto.setHTTPMethod("POST");
        httpRequestDto.setRequestTarget("/user/create");
        httpRequestDto.setHeader(new HashMap<>());
        Map<String, String> body = new HashMap<>();
        body.put("userId", "hello");
        body.put("password", "1111");
        body.put("name", "world");
        body.put("email", "hello@gmail.com");
        httpRequestDto.setBody(body);
        // when
        HTTPResponseDto actual = Controller.doRequest(httpRequestDto);
        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    // 로그인 실패 테스트
    @Test
    void testLoginFail() {
        // given
        HTTPResponseDto expected = new HTTPResponseDto("/user/login_failed.html");
        HTTPRequestDto httpRequestDto = new HTTPRequestDto();
        httpRequestDto.setHTTPMethod("POST");
        httpRequestDto.setRequestTarget("/user/login");
        httpRequestDto.setHeader(new HashMap<>());
        Map<String, String> body = new HashMap<>();
        body.put("userId", "login");
        body.put("password", "fail");
        httpRequestDto.setBody(body);
        // when
        HTTPResponseDto actual = Controller.doRequest(httpRequestDto);
        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    // 로그인 성공 테스트
    @Test
    void testLoginSuccess() {
        // given
        Database.addUser(new User("hello", "world", "hello", "world"));
        HTTPRequestDto httpRequestDto = new HTTPRequestDto();
        httpRequestDto.setHTTPMethod("POST");
        httpRequestDto.setRequestTarget("/user/login");
        httpRequestDto.setHeader(new HashMap<>());
        Map<String, String> body = new HashMap<>();
        body.put("userId", "hello");
        body.put("password", "world");
        httpRequestDto.setBody(body);
        // when
        HTTPResponseDto actual = Controller.doRequest(httpRequestDto);
        String location = actual.getHeader().get("Location");
        // then
        assertTrue(location.equals("/index.html"));
    }
}
