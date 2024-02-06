package controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import static org.assertj.core.api.Assertions.assertThat;

class StaticResourceControllerTest {

    @Test
    @DisplayName("html을 찾아 읽어서 응답하는 메서드 성공 테스트")
    void handleHtmlSuccessTest() throws IOException {
        StaticResourceController controller = StaticResourceController.getInstance();
        HttpRequest request = HttpRequest.createFromReader(new BufferedReader(
                new StringReader("GET /user/login.html HTTP/1.1\r\n" +
                        "\r\n")
        ));
        HttpResponse response = HttpResponse.of();

        controller.handle(request, response);

        assertThat(response.getHeaderProperty("Content-Length")).isNotBlank();
        assertThat(response.getHeaderProperty("Content-Type")).isEqualTo("text/html");
    }
    @Test
    @DisplayName("html을 찾아 읽어서 응답하는 메서드 실패 테스트")
    void handleHtmlFailTest() throws IOException {
        StaticResourceController controller = StaticResourceController.getInstance();
        HttpRequest request = HttpRequest.createFromReader(new BufferedReader(
                new StringReader("GET /invalid.html HTTP/1.1\r\n" +
                        "\r\n")
        ));
        HttpResponse response = HttpResponse.of();

        controller.handle(request, response);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("css를 찾아 읽고, 캐싱과 관련된 헤더도 함께 응답하는 메서드 실패 테스트")
    void handleCssSuccessTest() throws IOException {
        StaticResourceController controller = StaticResourceController.getInstance();
        HttpRequest request = HttpRequest.createFromReader(new BufferedReader(
                new StringReader("GET /js/scripts.js HTTP/1.1\r\n" +
                        "\r\n")
        ));
        HttpResponse response = HttpResponse.of();

        controller.handle(request, response);

        assertThat(response.getHeaderProperty("Content-Length")).isNotBlank();
        assertThat(response.getHeaderProperty("Content-Type")).isEqualTo("text/javascript");
        assertThat(response.getHeaderProperty("Cache-Control")).contains("max-age=30");
        assertThat(response.getHeaderProperty("Date")).isNotBlank();
        assertThat(response.getHeaderProperty("Last-Modified")).isNotBlank();
    }
}
