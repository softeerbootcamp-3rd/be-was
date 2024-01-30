package controller;

import db.Database;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import request.http.HttpRequest;
import response.http.HttpResponse;

import java.io.*;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static util.StatusCode.*;

class HomeControllerTest {
    private HomeController homeController;
    private Database database;
    @BeforeEach
    void init() {
        this.homeController = HomeController.getInstance();
        this.database = new Database();
    }

    @ParameterizedTest
    @MethodSource("status_200_Parameters")
    @DisplayName("요청 URI에 해당하는 파일이 존재하면 200 상태코드를 반환하는지 확인")
    void return_status_200(HttpRequest httpRequest) throws Exception {

        // when
        HttpResponse httpResponse = homeController.handleUserRequest(httpRequest);

        // then
        assertThat(httpResponse.getStatusCode()).isEqualTo(OK.getStatus());
    }

    @ParameterizedTest
    @MethodSource("status_302_Parameters")
    @DisplayName("루트 경로로 요청이 들어오면 302 상태코드를 반환하는지 확인")
    void return_status_302(HttpRequest httpRequest) throws Exception {

        // when
        HttpResponse httpResponse = homeController.handleUserRequest(httpRequest);

        // then
        assertThat(httpResponse.getStatusCode()).isEqualTo(FOUND.getStatus());
    }

    @ParameterizedTest
    @MethodSource("status_404_Parameters")
    @DisplayName("요청 URI에 해당하는 파일이 존재하지 않으면 404 상태코드를 반환하는지 확인")
    void return_status_404(HttpRequest httpRequest) throws Exception {

        // when
        HttpResponse httpResponse = homeController.handleUserRequest(httpRequest);

        // then
        assertThat(httpResponse.getStatusCode()).isEqualTo(NOT_FOUND.getStatus());
    }

    private static Stream<Arguments> status_200_Parameters() throws IOException {

        String httpRequestString = "GET /index.html HTTP/1.1\r\n"
                + "Content-Type: application/x-www-form-urlencoded\r\n"
                + "Cookie: sid=1234\r\n\r\n";
        InputStream inputStream = new ByteArrayInputStream(httpRequestString.getBytes("ISO-8859-1"));


        return Stream.of(
                Arguments.of(new HttpRequest(inputStream))
        );
    }

    private static Stream<Arguments> status_302_Parameters() throws IOException {

        String httpRequestString = "GET / HTTP/1.1\r\n"
                + "Content-Type: application/x-www-form-urlencoded\r\n"
                + "Cookie: sid=1234\r\n\r\n";
        InputStream inputStream = new ByteArrayInputStream(httpRequestString.getBytes("ISO-8859-1"));

        return Stream.of(
                Arguments.of(new HttpRequest(inputStream))
        );
    }

    private static Stream<Arguments> status_404_Parameters() throws IOException {

        String httpRequestString = "GET /indexs.html HTTP/1.1\r\n"
                + "Content-Type: application/x-www-form-urlencoded\r\n"
                + "Cookie: sid=1234\r\n\r\n";
        InputStream inputStream = new ByteArrayInputStream(httpRequestString.getBytes("ISO-8859-1"));

        return Stream.of(
                Arguments.of(new HttpRequest(inputStream))
        );
    }
}