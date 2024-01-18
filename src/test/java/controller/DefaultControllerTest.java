package controller;

import dto.HttpRequestDto;
import dto.HttpRequestDtoBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class DefaultControllerTest {
    private final DefaultController defaultController = new DefaultController();

    private final Logger logger = LoggerFactory.getLogger(DefaultControllerTest.class);

    @Test()
    @DisplayName("DefaultController.handleRequest() Test")
    public void handleRequestTest() {
        // given
        HttpRequestDto httpRequestDto = new HttpRequestDtoBuilder("GET", "/index.html", "HTTP/1.1").build();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(byteArrayOutputStream);

        // when
        defaultController.handleRequest(httpRequestDto, dos);

        // then
        String response = byteArrayOutputStream.toString();
        logger.debug(response);
        Assertions.assertThat(response).contains("HTTP/1.1 200 OK");
    }

    @Test()
    @DisplayName("DefaultController.handleRequest() Test - root(/) path")
    public void handleRequestRootTest() {
        // given
        HttpRequestDto httpRequestDto = new HttpRequestDtoBuilder("GET", "/", "HTTP/1.1").build();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(byteArrayOutputStream);

        // when
        defaultController.handleRequest(httpRequestDto, dos);

        // then
        String response = byteArrayOutputStream.toString();
        logger.debug(response);
        Assertions.assertThat(response).contains("HTTP/1.1 302 Found");
    }

    @Test()
    @DisplayName("DefaultController.handleRequest() Test - 404 Not Found")
    public void handleRequest404Test() {
        // given
        HttpRequestDto httpRequestDto = new HttpRequestDtoBuilder("GET", "/hello", "HTTP/1.1").build();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(byteArrayOutputStream);

        // when
        defaultController.handleRequest(httpRequestDto, dos);

        // then
        String response = byteArrayOutputStream.toString();
        logger.debug(response);
        Assertions.assertThat(response).contains("HTTP/1.1 404 Not Found");
    }
}
