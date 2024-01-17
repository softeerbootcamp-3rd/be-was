package util;

import data.RequestData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RequestParserUtilTest {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

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
        assertEquals("GET", requestData.getMethod());
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
}
