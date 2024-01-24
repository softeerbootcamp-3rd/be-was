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

    @Test
    @DisplayName("Post 정상 url 성공 테스트")
    public void postRequestServiceTest() throws IOException {
        // given
        String body = "userId=win9&password=password&name=seunggu&email=win9@gmail.com";
        HttpRequest request = HttpRequest.of(createHeader("POST", "/user/create", "HTTP/1.1"), body);

        // when
        CommonResponse response = FrontController.service(request);

        // then
        Assertions.assertEquals(HttpStatus.REDIRECT, response.getHttpStatus());
    }

    @Test
    @DisplayName("Post 비정상 url 예외 테스트")
    public void notValidPostRequestServiceTest() throws IOException {
        // given
        String body = "userId=win9&password=password&name=seunggu&email=win9@gmail.com";
        HttpRequest request = HttpRequest.of(createHeader("POST", "/user/bad", "HTTP/1.1"), body);

        // when
        CommonResponse response = FrontController.service(request);

        // then
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getHttpStatus());
    }

    private RequestHeader createHeader(String method, String path, String protocol) {
        return new RequestHeader(method, path, protocol);
    }

}