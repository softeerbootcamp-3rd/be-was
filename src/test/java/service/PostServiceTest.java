package service;

import db.Database;
import dto.HTTPRequestDto;
import dto.HTTPResponseDto;
import model.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PostServiceTest {

    // 로그인 실패 테스트
    @Test
    void testLoginFail() {
        // given
        HTTPResponseDto expected = new HTTPResponseDto(303, "/user/login_failed.html".getBytes());
        // when
        String body = "userId=login&password=fail";
        HTTPRequestDto httpRequestDto = new HTTPRequestDto("POST", "/user/login",
                "HTTP/1.1", "localhost:8080", "text/html", body.length(), body);
        HTTPResponseDto actual = PostService.login(httpRequestDto);
        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    // 로그인 성공 테스트
    @Test
    void testLoginSuccess() {
        // given
        HTTPResponseDto expected = new HTTPResponseDto(303, "/index.html".getBytes());
        // when
        Database.addUser(new User("hello", "world", "hello", "world"));
        String body = "userId=hello&password=world";
        HTTPRequestDto httpRequestDto = new HTTPRequestDto("POST", "/user/login",
                "HTTP/1.1", "localhost:8080", "text/html", body.length(), body);
        HTTPResponseDto actual = PostService.login(httpRequestDto);
        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }
}