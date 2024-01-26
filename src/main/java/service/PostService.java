package service;

import config.Config;
import db.Database;
import dto.request.HTTPRequestDto;
import dto.response.HTTPResponseDto;
import dto.session.Session;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.HashMap;

public class PostService {
    ///////////////////////////// POST 요청 처리 ///////////////////////////////////

    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    // 회원가입 요청 처리
    public HTTPResponseDto signup(HTTPRequestDto httpRequestDto) {
        HTTPResponseDto httpResponseDto = checkSignupBadRequest(httpRequestDto);

        if(httpRequestDto != null)      // 400 Bad Request일 경우
            return httpResponseDto;

        // 회원가입 정보 파싱
        HashMap<String, String> userInfo = httpRequestDto.bodyParsing();

        // User 객체 생성
        User user = new User(
                userInfo.get("userId"),
                userInfo.get("password"),
                userInfo.get("name"),
                userInfo.get("email"));

        // 유저 객체를 이용해서 회원가입 요청이 적잘한지 판단
        httpResponseDto = checkSignupWithUser(user);
        if(httpResponseDto != null)                     // 적절한 유저 요청이 아닐 경우
            return httpResponseDto;

        // 성공적인 회원가입 처리
        // 데이터베이스에 저장
        Database.addUser(user);
        logger.debug("새로운 유저: {}", user.toString());
        logger.debug("전체 DB: {}", Database.findAllUser());
        // /index.html로 리다이렉트
        return HTTPResponseDto.create302Dto("/index.html");
    }

    // 회원가입 요청이 bad request인지 판단
    private HTTPResponseDto checkSignupBadRequest(HTTPRequestDto httpRequestDto) {
        if(httpRequestDto == null || httpRequestDto.getRequestParams() == null || httpRequestDto.getBody() == null)
            return HTTPResponseDto.createResponseDto(400, "text/plain", "Bad Request".getBytes());

        String body = httpRequestDto.getBody();
        if(!body.contains("userId") || !body.contains("password")
                || !body.contains("name") || !body.contains("email"))
            return HTTPResponseDto.createResponseDto(400, "text/plain", "Bad Request".getBytes());

        return null;
    }

    // 유저 객체를 이용해서 회원가입 요청이 제대로 들어왔는지 판단
    private HTTPResponseDto checkSignupWithUser(User user) {
        // 필요한 정보가 제대로 들어오지 않았을 경우
        // 네가지 정보 모두 기입해야 회원가입 가능
        if(user.getUserId().equals("") || user.getPassword().equals("") || user.getName().equals("") || user.getEmail().equals(""))
            return HTTPResponseDto.createResponseDto(400, "text/plain", "모든 정보를 기입해주세요.".getBytes());

        // 중복 아이디 처리
        if(Database.findUserById(user.getUserId()) != null)
            return HTTPResponseDto.createResponseDto(200, "text/plain", "이미 존재하는 아이디입니다. 다시 시도해주세요.".getBytes());

        return null;
    }

    // 로그인 요청 처리
    public HTTPResponseDto login(HTTPRequestDto httpRequestDto) {
        // 1. request body가 null 일 경우
        if(httpRequestDto.getBody() == null)
            return HTTPResponseDto.createResponseDto(400, "text/plain", "Bad Request".getBytes());

        // body 파싱
        HashMap<String, String> bodyMap = httpRequestDto.bodyParsing();
        String userId = bodyMap.get("userId");
        String password = bodyMap.get("password");

        // 2. body에 userId, password 필드 네임이 없는 경우
        if(userId == null || password == null)
            return HTTPResponseDto.createResponseDto(400, "text/plain", "Bad Request".getBytes());

        // 3. 하나라도 빈 문자열이면 안됨
        if(userId.equals("") || password.equals(""))
            return HTTPResponseDto.createResponseDto(400, "text/plain", "Bad Request".getBytes());

        User user = Database.findUserById(userId);
        // 로그인 실패 1 : 아이디에 해당하는 유저가 없을 경우 or 비밀번호가 틀렸을 경우
        if(user == null || !user.getPassword().equals(password)) {
            HTTPResponseDto httpResponseDto = HTTPResponseDto.create302Dto("/user/login_failed.html");
            return httpResponseDto;
        }
        // 로그인 성공 -> 응답에 Set-Cookie 헤더 추가, index.html로 리다이렉트
        return loginSuccess(httpRequestDto, userId);
    }

    // 로그인 성공 시 세션 및 응답 처리
    private HTTPResponseDto loginSuccess(HTTPRequestDto httpRequestDto, String userId) {
        // 요청에 이미 세션이 있을 경우 기존 세션 아이디 가져오기
        String sessionId = httpRequestDto.getSessionId();

        Session session = null;
        boolean done = false;
        // 요청에 이미 세션이 있을 경우
        if(sessionId != null) {
            // session id 추출
            logger.debug("already exists session id: {}", sessionId);
            // 기존 세션 가져오기
            session = Database.findSessionById(sessionId);
            // 로그인한 아이디와 기존 세션의 아이디가 동일할 경우 -> 세션 재사용
            if(session != null && session.getUserId().equals(userId)) {
                session.setLastAccessTime(LocalDateTime.now());
                session.setExpires();
                logger.debug("login success - last access: {}", session.getLastAccessTime());
                done = true;
            }
        }
        // 세션을 가지지 않을 경우 or 기존 세션의 아이디가 현재 로그인한 아이디와 다를 경우
        if(!done) {
            // 세션 생성 후 저장소에 저장
            session = new Session(userId);
            Database.addSession(session);
            logger.debug("login success - new session created");
        }
        // http response 생성
        HTTPResponseDto httpResponseDto = HTTPResponseDto.create302Dto("/index.html");

        // 쿠키는 여러 개일 수 있으므로 value에 전체 헤더를 저장
        String setCookie = "sid=" + session.getId() + "; expires=" + session.getExpires() + "; Path=/; secure; HttpOnly\r\n";
        httpResponseDto.addHeader("Set-Cookie", "Set-Cookie: " + setCookie);
        return httpResponseDto;
    }
}
