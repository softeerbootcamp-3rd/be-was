package controller;

import dto.GetRequestEnum;
import dto.HTTPRequestDto;
import service.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class Controller {

    public static byte[] doRequest(HTTPRequestDto httpRequestDto, HashMap<String, String> requestParams) throws IOException {
        // 요청 url에 해당하는 enum 찾기
        byte[] result = "".getBytes();

        // 1. GET 요청
        if(httpRequestDto.getHTTP_Method().equals("GET")) {
            GetRequestEnum getRequestEnum = GetRequestEnum.getRequest(httpRequestDto.getRequest_target());
            // enum에 따라 함수 실행
            return getRequestEnum.doRequest(httpRequestDto, requestParams);
        }

        return result;
    }
}


