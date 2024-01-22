package service;

import config.Config;
import db.Database;
import dto.HTTPRequestDto;
import dto.HTTPResponseDto;
import model.User;

import java.util.HashMap;

public class PostService {
    ///////////////////////////// POST 요청 처리 ///////////////////////////////////

    // 회원가입 요청 처리
    public static HTTPResponseDto signup(HTTPRequestDto httpRequestDto) {
        if(httpRequestDto == null || httpRequestDto.getRequestParams() == null || httpRequestDto.getBody() == null)
            return new HTTPResponseDto(404, "Bad Request".getBytes());
        String body = httpRequestDto.getBody();
        if(!body.contains("userId") || !body.contains("password")
        || !body.contains("name") || !body.contains("email"))
            return new HTTPResponseDto(404, "Bad Request".getBytes());

        // 회원가입 정보 파싱
        HashMap<String, String> userInfo = httpRequestDto.bodyParsing();

        // User 객체 생성
        User user = new User(
                userInfo.get("userId"),
                userInfo.get("password"),
                userInfo.get("name"),
                userInfo.get("email"));

        // 필요한 정보가 제대로 들어오지 않았을 경우
        // 네가지 정보 모두 기입해야 회원가입 가능
        if(user.getUserId().equals("") || user.getPassword().equals("") || user.getName().equals("") || user.getEmail().equals(""))
            return new HTTPResponseDto(404, "모든 정보를 기입해주세요.".getBytes());

        // 중복 아이디 처리
        if(Database.findUserById(user.getUserId()) != null)
            return new HTTPResponseDto(200, "이미 존재하는 아이디입니다. 다시 시도해주세요.".getBytes());

        // 성공적인 회원가입 처리
        // 데이터베이스에 저장
        Database.addUser(user);
        Config.logger.debug("새로운 유저: {}", user.toString());
        Config.logger.debug("전체 DB: {}", Database.findAll());
        // /index.html로 리다이렉트
        return new HTTPResponseDto(302, "/index.html".getBytes());
    }

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
            return new HTTPResponseDto(302, "/user/login_failed.html".getBytes());
        }
        // 로그인 성공 -> index.html로 리다이렉트
        else {
            return new HTTPResponseDto(302, "/index.html".getBytes());
        }
    }
}
