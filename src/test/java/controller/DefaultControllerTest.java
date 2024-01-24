package controller;

import dto.HttpRequestDto;
import dto.HttpRequestDtoBuilder;
import dto.HttpResponseDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

public class DefaultControllerTest {
    private final DefaultController defaultController = new DefaultController();

    private final Logger logger = LoggerFactory.getLogger(DefaultControllerTest.class);

    @ParameterizedTest
    @MethodSource("handleRequestParams")
    @DisplayName("handleRequest(): 정적 페이지 요청 URL이 들어왔을 때 적절하게 처리해서 응답을 리턴한다.")
    public void handleRequestTest(String method, String uri, String expectedStatus) {
        // given
        HttpRequestDto httpRequestDto = new HttpRequestDtoBuilder(method, uri, "HTTP/1.1").build();

        // when
        HttpResponseDto httpResponseDto = defaultController.handleRequest(httpRequestDto);

        // then
        logger.debug(httpRequestDto.toString());
        logger.debug(new String(httpResponseDto.getBody(), StandardCharsets.UTF_8));
        Assertions.assertThat(httpResponseDto.getStatus()).isEqualTo(expectedStatus);

    }

    private static Stream<Arguments> handleRequestParams() {
        return Stream.of(
                Arguments.of("GET", "/index.html", "200"),
                Arguments.of("GET", "/", "302"),
                Arguments.of("GET", "/hello", "404")
        );
    }
}
