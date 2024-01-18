package service;

import db.Database;
import dto.HTTPRequestDto;
import dto.HTTPResponseDto;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ServiceTest {

    // 쿼리 스트링에 null 값이 들어왔을 경우 회원가입 테스트
    @Test
    void testSignUpNullParams() {
        // given
        HTTPResponseDto expected = new HTTPResponseDto(404, "Bad Request".getBytes());
        // when
        HTTPResponseDto actual = Service.signup(null);
        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    // 적절하지 않은 쿼리 스트링이 들어온 경우 회원가입 테스트
    @Test
    void testSignUpInvalidParams() {
        // given
        HTTPResponseDto expected = new HTTPResponseDto(404, "Bad Request".getBytes());
        // when
        HTTPRequestDto httpRequestDto = new HTTPRequestDto();
        httpRequestDto.addRequestParam("hello", "world");
        HTTPResponseDto actual = Service.signup(httpRequestDto);
        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    // 성공적인 회원가입 테스트
    @Test
    void testSignUpValidParams() {
        // given
        HTTPResponseDto expected = new HTTPResponseDto(303, "/index.html".getBytes());
        // when
        HTTPRequestDto httpRequestDto = new HTTPRequestDto("GET", "/user/create",
                "HTTP/1.1", "localhost:8080", "text/html", null, null);
        httpRequestDto.addRequestParam("userId", "bonnie1");
        httpRequestDto.addRequestParam("password", "1111");
        httpRequestDto.addRequestParam("name", "장보경");
        httpRequestDto.addRequestParam("email", "bonnie@gmail.com");
        HTTPResponseDto actual = Service.signup(httpRequestDto);
        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    // GET 메소드로 "/" url 요청이 왔을 경우
    @Test
    void testDefaultGET() {
        // given
        HTTPResponseDto expected = new HTTPResponseDto(303, "/index.html".getBytes());
        // when
        HTTPRequestDto httpRequestDto = new HTTPRequestDto("GET", "/",
                "HTTP/1.1", "localhost:8080", "text/html", null, null);
        HTTPResponseDto actual = Service.showIndex(httpRequestDto);
        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    // request target으로 null이 들어왔을 경우 파일 리턴 함수 테스트
    @Test
    void testRequestNullFile() {
        try {
            // given
            HTTPResponseDto expected = new HTTPResponseDto(404, "Bad Request".getBytes());
            // when
            HTTPRequestDto httpRequestDto = new HTTPRequestDto();
            HTTPResponseDto actual = Service.requestFile(httpRequestDto);
            // then
            assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        }
        catch(IOException e) {
            fail();
        }
    }

    // index.html Get 요청이 들어왔을 경우 파일 리턴 함수 테스트
    @Test
    void testRequestIndexHtml() {
        try {
            // given
            String path = "./src/main/resources/templates/index.html";
            HTTPResponseDto expected = new HTTPResponseDto(200, Files.readAllBytes(new File(path).toPath()));
            // when
            HTTPRequestDto httpRequestDto = new HTTPRequestDto("GET", "/index.html",
                    "HTTP/1.1", "localhost:8080", "text/html", null, null);
            HTTPResponseDto actual = Service.requestFile(httpRequestDto);
            // then
            assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        }
        catch(IOException e) {
            fail();
        }
    }

    // style.css 용청이 들어왔을 경우 파일 리턴 함수
    @Test
    void testRequestStyleCss() {
        try {
            // given
            String path = "./src/main/resources/static/css/styles.css";
            HTTPResponseDto expected = new HTTPResponseDto(200, Files.readAllBytes(new File(path).toPath()));
            // when
            HTTPRequestDto httpRequestDto = new HTTPRequestDto("GET", "/css/styles.css",
                    "HTTP/1.1", "localhost:8080", "text/css", null, null);
            HTTPResponseDto actual = Service.requestFile(httpRequestDto);
            // then
            assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        }
        catch(IOException e) {
            fail();
        }
    }
}