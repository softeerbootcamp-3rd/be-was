package controller;

import dto.HTTPRequestDto;
import service.Service;

import java.io.IOException;
import java.util.Arrays;

public class Controller {

    public static byte[] doRequest(HTTPRequestDto httpRequestDto, String[] requestParams) throws IOException {
        // 요청 url에 해당하는 enum 찾기
        Request request = Request.getRequest(httpRequestDto.getRequest_target());
        // enum에 따라 함수 실행
        return request.doRequest(httpRequestDto, requestParams);
    }
}

enum Request {

    SIGNUP("/user/create") {
        @Override
        byte[] doRequest(HTTPRequestDto httpRequestDto, String[] requestParams) {
            return Service.signup(requestParams);
        }
    },
    FILE("file") {
        @Override
        byte[] doRequest(HTTPRequestDto httpRequestDto, String[] requestParams) throws IOException {
            return Service.requestFile(httpRequestDto);

        }
    };

    private String url;

    Request(String url) {
        this.url = url;
    }

    static Request getRequest(String url) {
        return Arrays.stream(Request.values())
                .filter(request -> request.url.equals(url))
                .findFirst()
                .orElse(FILE);
    }

    abstract byte[] doRequest(HTTPRequestDto httpRequestDto, String[] requestParams) throws IOException;
}


