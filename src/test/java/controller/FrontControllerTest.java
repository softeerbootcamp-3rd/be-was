package controller;

import model.CommonResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.HttpRequest;
import util.HttpStatus;
import webserver.RequestHeader;

import java.io.IOException;

class FrontControllerTest {


    @Test
    @DisplayName("정상 url 응답 테스트")
    public void validUrlServiceTest() throws IOException {
        // given
        HttpRequest request = HttpRequest.of(createHeader("GET", "/index.html", "HTTP/1.1"), "dummyData");

        // when
        CommonResponse response = FrontController.service(request);

        // then
        Assertions.assertEquals("html", response.getExtension());
        Assertions.assertEquals(HttpStatus.OK, response.getHttpStatus());
    }

    @Test
    @DisplayName("잘못된 url 요청 응답 테스트")
    public void notValidUrlServiceTest() throws IOException {
        // given
        HttpRequest request = HttpRequest.of(createHeader("GET", "/index/bad", "HTTP/1.1"), "dummyData");

        // when
        CommonResponse response = FrontController.service(request);

        // then
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getHttpStatus());
    }

    @Test
    @DisplayName("잘못된 static 자원 요청 응답 테스트")
    public void notValidResourceServiceTest() throws IOException {
        // given
        HttpRequest request = HttpRequest.of(createHeader("GET", "/css/badcss.css", "HTTP/1.1"), "dummyData");


        // when
        CommonResponse response = FrontController.service(request);

        // then
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getHttpStatus());
    }

    private RequestHeader createHeader(String method, String path, String protocol) {
        return new RequestHeader(method, path, protocol);
    }

}