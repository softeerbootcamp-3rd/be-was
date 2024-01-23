package controller;

import dto.GetRequestEnum;
import dto.HTTPRequestDto;
import dto.HTTPResponseDto;
import dto.PostRequestEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.WebServer;

import java.io.IOException;

public class Controller {

    private static final Logger logger = LoggerFactory.getLogger(Controller.class);
    public static HTTPResponseDto doRequest(HTTPRequestDto httpRequestDto) throws IOException {
        HTTPResponseDto response = new HTTPResponseDto();
        // 1. GET 요청
        if(httpRequestDto.getHTTPMethod().equals("GET")) {
            GetRequestEnum getRequestEnum = GetRequestEnum.getRequest(httpRequestDto.getRequestTarget());
            // enum에 따라 함수 실행
            return getRequestEnum.doRequest(httpRequestDto);
        }
        // 2. POST 요청
        if(httpRequestDto.getHTTPMethod().equals("POST")) {
            PostRequestEnum postRequestEnum = PostRequestEnum.getRequest(httpRequestDto.getRequestTarget());
            // enum에 따라 함수 실행
            return postRequestEnum.doRequest(httpRequestDto);
        }

        return response;
    }
}


