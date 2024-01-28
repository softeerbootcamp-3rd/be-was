package service;

import config.Config;
import controller.Controller;
import db.Database;
import dto.request.HTTPRequestDto;
import dto.response.HTTPResponseDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class GetServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(GetServiceTest.class);

    @AfterEach
    void clearDB() {
        logger.debug("User DB: {}", Database.findAllUser());
        logger.debug("Session DB: {}", Database.findAllUser());
        Database.clearUsers();
        Database.clearSessions();
    }

    @DisplayName(" / 요청이 들어왔을 경우 index.html로 리다이렉트")
    @Test
    void testDefaultGET() {
        // given
        HTTPResponseDto expected = new HTTPResponseDto("/index.html");
        HTTPRequestDto httpRequestDto = new HTTPRequestDto();
        httpRequestDto.setHTTPMethod("GET");
        httpRequestDto.setRequestTarget("/");
        // when
        HTTPResponseDto actual = Controller.doRequest(httpRequestDto);
        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName(" /user/list.html 요청 처리 1 - 로그인되어 있지 않을 경우 로그인 화면으로 리다이렉트")
    @Test
    void testShowUserListFail() {
        // given
        HTTPResponseDto expected = new HTTPResponseDto("/user/login.html");
        HTTPRequestDto httpRequestDto = new HTTPRequestDto();
        httpRequestDto.setHTTPMethod("GET");
        httpRequestDto.setRequestTarget("/user/list.html");
        httpRequestDto.setHeader(new HashMap<>());
        // when
        HTTPResponseDto actual = Controller.doRequest(httpRequestDto);
        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName(" /user/list.html 요청 처리 2 - 로그인되어 있을 경우 사용자 목록 반환 요청 정상 처리")
    @Test
    void testShowUserListSuccess() {
        // given
        HTTPRequestDto httpRequestDto = new HTTPRequestDto();
        httpRequestDto.setHTTPMethod("GET");
        httpRequestDto.setRequestTarget("/user/list.html");
        Map<String, String> header = new HashMap<>();
        header.put("Cookie", "sid=1234");
        httpRequestDto.setHeader(header);

        // when
        HTTPResponseDto actual = Controller.doRequest(httpRequestDto);
        // then
        assertTrue(actual.getStatusCode() == 200);
    }

    @DisplayName(" /user/profile.html 요청 처리 1 - 로그인되어 있지 않을 경우 로그인 화면으로 리다이렉트")
    @Test
    void testShowUserProfileFail() {
        // given
        HTTPResponseDto expected = new HTTPResponseDto("/user/login.html");
        HTTPRequestDto httpRequestDto = new HTTPRequestDto();
        httpRequestDto.setHTTPMethod("GET");
        httpRequestDto.setRequestTarget("/user/profile.html");
        httpRequestDto.setHeader(new HashMap<>());
        // when
        HTTPResponseDto actual = Controller.doRequest(httpRequestDto);
        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName(" /user/profile.html 요청 처리 2 - 로그인되어 있을 경우 마이페이지 요청 정상 처리")
    @Test
    void testShowUserProfileSuccess() {
        // given
        HTTPRequestDto httpRequestDto = new HTTPRequestDto();
        httpRequestDto.setHTTPMethod("GET");
        httpRequestDto.setRequestTarget("/user/profile.html");
        Map<String, String> header = new HashMap<>();
        header.put("Cookie", "sid=1234");
        httpRequestDto.setHeader(header);

        // when
        HTTPResponseDto actual = Controller.doRequest(httpRequestDto);
        // then
        assertTrue(actual.getStatusCode() == 200);
    }

    @DisplayName("request target으로 null이 들어왔을 경우 파일 리턴 함수가 400을 응답하는지 테스트")
    @Test
    void testRequestNullFile() {
        // given
        HTTPResponseDto expected = new HTTPResponseDto(400, "text/plain", "Bad Request".getBytes());
        HTTPRequestDto httpRequestDto = new HTTPRequestDto();
        // when
        HTTPResponseDto actual = Config.httpGetService.requestFile(httpRequestDto);
        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName(" /index.html 요청이 들어왔을 경우 정적 파일 리턴 테스트")
    @Test
    void testRequestIndexHtml() {
        try {
            // given
            String path = "./src/main/resources/templates/index.html";
            HTTPResponseDto expected = new HTTPResponseDto(200, "text/html",
                    Files.readAllBytes(new File(path).toPath()));
            HTTPRequestDto httpRequestDto = new HTTPRequestDto();
            httpRequestDto.setHTTPMethod("GET");
            httpRequestDto.setRequestTarget("/index.html");
            Map<String, String> header = new HashMap<>();
            header.put("Accept", "text/html");
            httpRequestDto.setHeader(header);
            // when
            HTTPResponseDto actual = Controller.doRequest(httpRequestDto);
            // then
            assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        }
        catch(IOException e) {
            fail();
        }
    }

    @DisplayName(" /css/styles.css 요청이 들어왔을 경우 파일 리턴 테스트")
    @Test
    void testRequestStyleCss() {
        try {
            // given
            String path = "./src/main/resources/static/css/styles.css";
            HTTPResponseDto expected = new HTTPResponseDto(200, "text/css",
                    Files.readAllBytes(new File(path).toPath()));
            HTTPRequestDto httpRequestDto = new HTTPRequestDto();
            httpRequestDto.setHTTPMethod("GET");
            httpRequestDto.setRequestTarget("/css/styles.css");
            Map<String, String> header = new HashMap<>();
            header.put("Accept", "text/css");
            httpRequestDto.setHeader(header);
            // when
            HTTPResponseDto actual = Config.httpGetService.requestFile(httpRequestDto);
            // then
            assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        }
        catch(IOException e) {
            fail();
        }
    }

}
