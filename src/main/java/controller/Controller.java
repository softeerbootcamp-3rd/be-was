package controller;

import dto.GetRequestEnum;
import dto.HTTPRequestDto;
import dto.HTTPResponseDto;
import java.io.IOException;

public class Controller {

    public static HTTPResponseDto doRequest(HTTPRequestDto httpRequestDto) throws IOException {
        HTTPResponseDto response = new HTTPResponseDto();
        // 1. GET 요청
        if(httpRequestDto.getHTTPMethod().equals("GET")) {
            GetRequestEnum getRequestEnum = GetRequestEnum.getRequest(httpRequestDto.getRequestTarget());
            // enum에 따라 함수 실행
            return getRequestEnum.doRequest(httpRequestDto);
        }

        return response;
    }
}


