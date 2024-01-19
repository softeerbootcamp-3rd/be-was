package controller;

import dto.HttpRequestDto;
import dto.HttpRequestDtoBuilder;
import dto.HttpResponseDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

public class UserControllerTest {
    private final UserController userController = new UserController();

    private final Logger logger = LoggerFactory.getLogger(UserControllerTest.class);

    @ParameterizedTest
    @MethodSource("handleRequestParams")
    @DisplayName("UserController.handleRequest() Test")
    public void handleRequestTest(String method, String uri, String expectedStatus) {
        // given
        HttpRequestDto httpRequestDto = new HttpRequestDtoBuilder(method, uri, "HTTP/1.1").build();

        // when
        HttpResponseDto httpResponseDto = userController.handleRequest(httpRequestDto);

        // then
        logger.debug(httpRequestDto.toString());
        logger.debug(new String(httpResponseDto.getBody()), StandardCharsets.UTF_8);
        Assertions.assertThat(httpResponseDto.getStatus()).contains(expectedStatus);
    }

    public static Stream<Arguments> handleRequestParams() {
        return Stream.of(
                // 200 OK
                Arguments.of("GET", "/user/form.html", "200"),
                // 302 Found
                Arguments.of("GET", "/user/create?userId=userId&password=password&name=name&email=softeer@example.com", "302"),
                // 400 Bad Request: /user/create 시 필요한 파라미터를 모두 전달하지 않은 경우
                Arguments.of("GET", "/user/create?userId=userId", "400")
        );
    }
}
