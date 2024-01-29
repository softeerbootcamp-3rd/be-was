package util;

import controller.HttpMethod;
import controller.HttpStatusCode;
import data.RequestData;
import data.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RequestParserUtilTest {

    private static final Logger logger = LoggerFactory.getLogger(RequestParserUtilTest.class);

    @DisplayName("올바른 HTTP Request")
    @Test
    public void parseRequest_ValidInput_ReturnRequestData() throws IOException {
        // Given
        String requestString = "GET /example/path HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Connection: keep-alive\r\n" +
                "Accept: */*\r\n\r\n";
        BufferedReader br = new BufferedReader(new StringReader(requestString));

        // When
        RequestData requestData = RequestParserUtil.parseRequest(br);

        // Then
        assertEquals(HttpMethod.GET, requestData.getMethod());
        assertEquals("/example/path", requestData.getRequestContent());
        assertEquals("HTTP/1.1", requestData.getHttpVersion());
    }

    @DisplayName("올바르지 않은 HTTP Request")
    @Test
    void parseRequest_InvalidInput_ReturnRequestData() {
        // Given
        String invalidRequestString = "Invalid Request\r\n";
        BufferedReader br = new BufferedReader(new StringReader(invalidRequestString));

        // When, Then
        assertThrows(Exception.class, () -> RequestParserUtil.parseRequest(br)); // 예외타입을 IOException으로 하면 해당 오류에만 test를 검증하게 됨. 전체적으로 보기 위해 Exception으로 검증
    }

    @Test
    @DisplayName("query 필드 별 파싱 테스트")
    void testParseUserRegisterQuery() {
        // Given
        String url = "key1=value1&key2=value2&key3=value3";

        // When
        Map<String, String> actual = RequestParserUtil.parseUserRegisterQuery(url);

        // Then
        assertThat(actual)
                .containsEntry("key1", "value1")
                .containsEntry("key2", "value2")
                .containsEntry("key3", "value3");
    }

    @Test
    @DisplayName("HTTP 헤더 파싱 시 trailing white space 처리")
    void testParseHeadersWithTrailingWhiteSpace() throws IOException {
        // Given
        String requestString = "GET /example/path HTTP/1.1\r\n" +
                "Host : localhost:8080\r\n" + // 헤더에 trailing white space 추가
                "Connection: keep-alive  \r\n" + // 헤더 값에 trailing white space 추가
                "Accept: */*\r\n\r\n";
        BufferedReader br = new BufferedReader(new StringReader(requestString));

        // When
        RequestData requestData = RequestParserUtil.parseRequest(br);

        // Then
        assertEquals(HttpMethod.GET, requestData.getMethod());
        assertEquals("/example/path", requestData.getRequestContent());
        assertEquals("HTTP/1.1", requestData.getHttpVersion());
        // 헤더의 trailing white space가 제거되어야 함
        assertThat(requestData.getHeaders())
                .containsEntry("Host", "localhost:8080")
                .containsEntry("Connection", "keep-alive")
                .containsEntry("Accept", "*/*");
    }
}
