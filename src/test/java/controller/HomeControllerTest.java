package controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import request.http.HttpRequest;
import response.http.HttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class HomeControllerTest {
    private HomeController homeController;

    @BeforeEach
    void init() {
        this.homeController = HomeController.getInstance();
    }

    @ParameterizedTest
    @MethodSource("status_200_Parameters")
    @DisplayName("요청 URI에 해당하는 파일이 존재하면 200 상태코드를 반환하는지 확인")
    void return_status_200(HttpRequest httpRequest) throws Exception {

        // when
        HttpResponse httpResponse = homeController.handleUserRequest(httpRequest);

        // then
        assertThat(httpResponse.getStatusCode()).isEqualTo(200);
    }

    @ParameterizedTest
    @MethodSource("status_302_Parameters")
    @DisplayName("루트 경로로 요청이 들어오면 302 상태코드를 반환하는지 확인")
    void return_status_302(HttpRequest httpRequest) throws Exception {

        // when
        HttpResponse httpResponse = homeController.handleUserRequest(httpRequest);

        // then
        assertThat(httpResponse.getStatusCode()).isEqualTo(302);
    }

    @ParameterizedTest
    @MethodSource("status_404_Parameters")
    @DisplayName("요청 URI에 해당하는 파일이 존재하지 않으면 404 상태코드를 반환하는지 확인")
    void return_status_404(HttpRequest httpRequest) throws Exception {

        // when
        HttpResponse httpResponse = homeController.handleUserRequest(httpRequest);

        // then
        assertThat(httpResponse.getStatusCode()).isEqualTo(404);
    }

    private static Stream<Arguments> status_200_Parameters() throws IOException {

        return Stream.of(
                Arguments.of(new HttpRequest(new BufferedReader(new StringReader("GET /index.html HTTP/1.1\n" +
                        "Host: localhost:8080\n" +
                        "Connection: keep-alive\n" +
                        "Accept: */*\n" +
                        "\n"))))
        );
    }

    private static Stream<Arguments> status_302_Parameters() throws IOException {

        return Stream.of(
                Arguments.of(new HttpRequest(new BufferedReader(new StringReader("GET / HTTP/1.1\n" +
                        "Host: localhost:8080\n" +
                        "Connection: keep-alive\n" +
                        "Accept: */*\n" +
                        "\n"))))
        );
    }

    private static Stream<Arguments> status_404_Parameters() throws IOException {

        return Stream.of(
                Arguments.of(new HttpRequest(new BufferedReader(new StringReader("GET /test.html HTTP/1.1\n" +
                        "Host: localhost:8080\n" +
                        "Connection: keep-alive\n" +
                        "Accept: */*\n" +
                        "\n"))))
        );
    }
}