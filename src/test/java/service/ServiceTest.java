package service;

import dto.HTTPRequestDto;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class ServiceTest {

    // 쿼리 스트링에 null 값이 들어왔을 경우 회원가입 테스트
    @Test
    void testSignUpNullParams() {
        try {
            // 기댓값
            byte[] answer = "다시 시도해주세요.".getBytes();
            // 실행값
            byte[] result = Service.signup(null, null);
            assertArrayEquals(answer, result);
        }
        catch(IOException e) {
            fail();
        }
    }

    // 적절하지 않은 쿼리 스트링이 들어온 경우 회원가입 테스트
    @Test
    void testSignUpInvalidParams() {
        try {
            // 기댓값
            byte[] answer = "다시 시도해주세요.".getBytes();
            // 실행값
            HTTPRequestDto httpRequestDto = new HTTPRequestDto();
            HashMap<String, String> map = new HashMap<>();
            byte[] result = Service.signup(httpRequestDto, map);
            assertArrayEquals(answer, result);
        }
        catch(IOException e) {
            fail();
        }
    }

    // 성공적인 회원가입 테스트
    @Test
    void testSignUpValidParams() {
        try {
            HTTPRequestDto httpRequestDto = new HTTPRequestDto("GET", "/user/create",
                    "HTTP/1.1", "localhost:8080", "text/html");
            HashMap<String, String> requestParams = new HashMap<>();
            requestParams.put("userId", "bonnie");
            requestParams.put("password", "1111");
            requestParams.put("name", "장보경");
            requestParams.put("email", "bonnie@gmail.com");
            // 기댓값
            byte[] answer = "Hello, 장보경!".getBytes();
            assertArrayEquals(answer, Service.signup(httpRequestDto, requestParams));
        }
        catch(IOException e) {
            fail();
        }
    }

    // request target으로 null이 들어왔을 경우 파일 리턴 함수 테스트
    @Test
    void testRequestNullFile() {
        try {
            // 기댓값
            byte[] answer = "다시 시도해주세요.".getBytes();
            HTTPRequestDto httpRequestDto = new HTTPRequestDto();
            assertArrayEquals(answer, Service.requestFile(httpRequestDto));
        }
        catch(IOException e) {
            fail();
        }
    }

    // index.html Get 요청이 들어왔을 경우 파일 리턴 함수 테스트
    @Test
    void testRequestIndexHtml() {
        try {
            // 기댓값
            String path = "./src/main/resources/templates/index.html";
            byte[] answer = Files.readAllBytes(new File(path).toPath());
            // 실행값
            HTTPRequestDto httpRequestDto = new HTTPRequestDto("GET", "/index.html",
                    "HTTP/1.1", "localhost:8080", "text/html");
            byte[] result = Service.requestFile(httpRequestDto);
            assertArrayEquals(answer, result);
        }
        catch(IOException e) {
            fail();
        }
    }

    // style.css 용청이 들어왔을 경우 파일 리턴 함수
    @Test
    void testRequestStyleCss() {
        try {
            // 기댓값
            String path = "./src/main/resources/static/css/styles.css";
            byte[] answer = Files.readAllBytes(new File(path).toPath());
            // 실행값
            HTTPRequestDto httpRequestDto = new HTTPRequestDto("GET", "/css/styles.css",
                    "HTTP/1.1", "localhost:8080", "text/css");
            byte[] result = Service.requestFile(httpRequestDto);
            assertArrayEquals(answer, result);
        }
        catch(IOException e) {
            fail();
        }
    }
}