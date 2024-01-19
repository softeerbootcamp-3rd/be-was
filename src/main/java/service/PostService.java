package service;

import db.Database;
import dto.HTTPRequestDto;
import dto.HTTPResponseDto;

public class PostService {
    ///////////////////////////// POST 요청 처리 ///////////////////////////////////

    // 로그인 요청 처리
    public static HTTPResponseDto login(HTTPRequestDto httpRequestDto) {
        // 1. request body가 null 일 경우
        if(httpRequestDto.getBody() == null)
            return new HTTPResponseDto(404, "Bad Request".getBytes());
        // 2. body에 userId, password 필드 네임이 없는 경우
        if( !(httpRequestDto.getBody().contains("userId") && httpRequestDto.getBody().contains("password")) )
            return new HTTPResponseDto(404, "Bad Request".getBytes());

        // body 파싱
        String[] tokens = httpRequestDto.getBody().split("&");
        String userId = tokens[0].substring("userId=".length());
        String password = tokens[1].substring("password=".length());

        // 3. 둘 다 빈 문자열이면 안됨
        if(userId.equals("") || password.equals(""))
            return new HTTPResponseDto(404, "Bad Request".getBytes());

        // 로그인 실패
        if(Database.findUserById(userId) == null) {
            return new HTTPResponseDto(303, "/user/login_failed.html".getBytes());
        }
        // 로그인 성공 -> index.html로 리다이렉트
        else {
            return new HTTPResponseDto(303, "/index.html".getBytes());
        }
    }
}
