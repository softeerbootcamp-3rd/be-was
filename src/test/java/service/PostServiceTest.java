package service;

import config.Config;
import db.Database;
import dto.HTTPRequestDto;
import dto.HTTPResponseDto;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.WebServer;

import static org.assertj.core.api.Assertions.assertThat;

class PostServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(PostServiceTest.class);

    @AfterEach
    void clearDB() {
        logger.debug("DB: {}", Database.findAll());
        Database.clear();
    }

    // 회원가입 테스트 1: 바디에 key 값이 제대로 들어오지 않은 경우
    @Test
    void testSignUpBadKeys() {
        // given
        HTTPResponseDto expected = new HTTPResponseDto(404, "Bad Request".getBytes());
        // when
        HTTPRequestDto httpRequestDto = new HTTPRequestDto("POST", "/user/create",
                "HTTP/1.1", "localhost:8080", "text/html", null, null);
        String body = "userid=hello&password=1111&name=hello&email=hello@gmail.com";        // userid -> userId
        httpRequestDto.setBody(body);
        HTTPResponseDto actual = Config.postService.signup(httpRequestDto);
        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    // 회원가입 테스트 2: 회원 정보를 다 입력하지 않았을 경우 (빈 문자열)
    @Test
    void testSignUpBadValues() {
        // given
        HTTPResponseDto expected = new HTTPResponseDto(404, "모든 정보를 기입해주세요.".getBytes());
        // when
        HTTPRequestDto httpRequestDto = new HTTPRequestDto("POST", "/user/create",
                "HTTP/1.1", "localhost:8080", "text/html", null, null);
        String body = "userId=hello&password=1111&name=hello&email=";       // email이 빈 문자열로 들어온 상황
        httpRequestDto.setBody(body);
        HTTPResponseDto actual = Config.postService.signup(httpRequestDto);
        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    // 회원가입 테스트 3: 중복 회원이 존재할 경우
    @Test
    void testSignUpAlreadyExists() {
        // given
        HTTPResponseDto expected = new HTTPResponseDto(200, "이미 존재하는 아이디입니다. 다시 시도해주세요.".getBytes());
        // when
        // 첫 번째 유저 회원가입 처리
        HTTPRequestDto httpRequestDto = new HTTPRequestDto("POST", "/user/create",
                "HTTP/1.1", "localhost:8080", "text/html", null, null);
        String body = "userId=hello&password=1111&name=hello&email=hello@gmail.com";
        httpRequestDto.setBody(body);
        Config.postService.signup(httpRequestDto);
        // 두 번째 유저 회원가입 처리
        httpRequestDto = new HTTPRequestDto("POST", "/user/create",
                "HTTP/1.1", "localhost:8080", "text/html", null, null);
        body = "userId=hello&password=2222&name=hello2&email=hello2@gmail.com";
        httpRequestDto.setBody(body);
        HTTPResponseDto actual = Config.postService.signup(httpRequestDto);
        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    // 회원가입 테스트 4: 성공
    @Test
    void testSignUpSuccess() {
        // given
        HTTPResponseDto expected = new HTTPResponseDto(302, "/index.html".getBytes());
        // when
        HTTPRequestDto httpRequestDto = new HTTPRequestDto("POST", "/user/create",
                "HTTP/1.1", "localhost:8080", "text/html", null, null);
        String body = "userId=hello&password=1111&name=hello&email=hello@gmail.com";
        httpRequestDto.setBody(body);
        HTTPResponseDto actual = Config.postService.signup(httpRequestDto);
        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    // 로그인 실패 테스트
    @Test
    void testLoginFail() {
        // given
        HTTPResponseDto expected = new HTTPResponseDto(302, "/user/login_failed.html".getBytes());
        // when
        String body = "userId=login&password=fail";
        HTTPRequestDto httpRequestDto = new HTTPRequestDto("POST", "/user/login",
                "HTTP/1.1", "localhost:8080", "text/html", body.length(), body);
        HTTPResponseDto actual = Config.postService.login(httpRequestDto);
        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    // 로그인 성공 테스트
    @Test
    void testLoginSuccess() {
        // given
        HTTPResponseDto expected = new HTTPResponseDto(302, "/index.html".getBytes());
        // when
        Database.addUser(new User("hello", "world", "hello", "world"));
        String body = "userId=hello&password=world";
        HTTPRequestDto httpRequestDto = new HTTPRequestDto("POST", "/user/login",
                "HTTP/1.1", "localhost:8080", "text/html", body.length(), body);
        HTTPResponseDto actual = Config.postService.login(httpRequestDto);
        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }
}
