package controller;

import annotation.PostMapping;
import dto.RequestDto;
import dto.ResponseDto;
import service.UserService;
import session.SessionManager;

import static constant.StaticFile.MAIN_PAGE;

public class UserController {

    @PostMapping(path = "/user/create")
    public static ResponseDto create(RequestDto requestDto) {
        UserService.signUp(requestDto.getBody());

        return new ResponseDto().makeRedirect(MAIN_PAGE.path);
    }

    @PostMapping(path = "/user/login")
    public static ResponseDto login(RequestDto requestDto) {
        ResponseDto response = new ResponseDto();
        response.makeRedirect(MAIN_PAGE.path);

        // 이전에 로그인한 유저 (요청 헤더 쿠키에 sessionId 가 있음) && 세션이 만료되지 않은 경우
        // MAIN_PAGE 로 리다이렉트
        String sessionId = requestDto.getCookies().get("sessionId");
        if (sessionId != null && SessionManager.getSession(sessionId) != null) {
            return response;
        }

        // 처음 로그인한 유저 (요청 헤더 쿠키에 sessionId 없음) OR 세션이 만료된 경우
        String userId = requestDto.getBody().get("userId");
        String password = requestDto.getBody().get("password");
        // UserService.login 하고 생성한 유저의 sessionId 값을 응답 헤더에 추가해 응답 리턴
        response.addHeader("Set-Cookie", ("sessionId=" + UserService.login(userId, password) + "; Path=/"));

        return response;
    }
}
