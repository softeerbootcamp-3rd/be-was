package controller;

import dto.request.GetRequestEnum;
import dto.request.HTTPRequestDto;
import dto.response.HTTPResponseDto;
import dto.request.PostRequestEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Controller {

    private static final Logger logger = LoggerFactory.getLogger(Controller.class);
    public static HTTPResponseDto doRequest(HTTPRequestDto httpRequestDto) {
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

        return null;
    }
}


