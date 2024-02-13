package service;

import db.Database;
import dto.request.FirstClassCollection;
import dto.request.HTTPRequestDto;
import dto.response.HTTPResponseDto;
import model.Post;
import model.Session;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Map;

public class PostService {
    ///////////////////////////// POST 요청 처리 ///////////////////////////////////

    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    // 회원가입 요청 처리
    public HTTPResponseDto signup(HTTPRequestDto httpRequestDto) {
        HTTPResponseDto httpResponseDto = checkSignupBadRequest(httpRequestDto);

        if(httpResponseDto != null)      // 400 Bad Request일 경우
            return httpResponseDto;

        // User 객체 생성
        User user = new User(
                httpRequestDto.getBody().getValue("userId"),
                httpRequestDto.getBody().getValue("password"),
                httpRequestDto.getBody().getValue("name"),
                httpRequestDto.getBody().getValue("email"));

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
        return new HTTPResponseDto("/index.html");
    }

    // 회원가입 요청이 bad request인지 판단
    private HTTPResponseDto checkSignupBadRequest(HTTPRequestDto httpRequestDto) {
        if(httpRequestDto == null || httpRequestDto.getBody() == null)
            return new HTTPResponseDto(400, "text/plain", "Bad Request".getBytes());
        // 이미 로그인 되어있을 경우
        if(httpRequestDto.getSessionId() != null)
            return new HTTPResponseDto(400, "text/plain", "이미 로그인 되어있습니다.".getBytes());
        Map<String, String> body = httpRequestDto.getBody().getMap();
        if(!body.containsKey("userId") || !body.containsKey("password")
                || !body.containsKey("name") || !body.containsKey("email"))
            return new HTTPResponseDto(400, "text/plain", "Bad Request".getBytes());
        return null;
    }

    // 유저 객체를 이용해서 회원가입 요청이 제대로 들어왔는지 판단
    private HTTPResponseDto checkSignupWithUser(User user) {
        // 필요한 정보가 제대로 들어오지 않았을 경우
        // 네가지 정보 모두 기입해야 회원가입 가능
        if(user.getUserId().equals("") || user.getPassword().equals("") || user.getName().equals("") || user.getEmail().equals(""))
            return new HTTPResponseDto(400, "text/plain", "모든 정보를 기입해주세요.".getBytes());

        // 중복 아이디 처리
        if(Database.findUserById(user.getUserId()) != null)
            return new HTTPResponseDto(200, "text/plain", "이미 존재하는 아이디입니다. 다시 시도해주세요.".getBytes());
        return null;
    }

    // 로그인 요청 처리
    public HTTPResponseDto login(HTTPRequestDto httpRequestDto) {
        HTTPResponseDto httpResponseDto = checkLoginBadRequest(httpRequestDto);
        if(httpResponseDto != null)
            return httpResponseDto;

        String userId = httpRequestDto.getBody().getValue("userId");
        String password = httpRequestDto.getBody().getValue("password");

        User user = Database.findUserById(userId);
        // 로그인 실패 1 : 아이디에 해당하는 유저가 없을 경우 or 비밀번호가 틀렸을 경우
        if(user == null || !user.getPassword().equals(password))
            return new HTTPResponseDto("/user/login_failed.html");
        // 로그인 성공 -> 응답에 Set-Cookie 헤더 추가, index.html로 리다이렉트
        return loginSuccess(httpRequestDto, userId);
    }

    // 로그인 요청이 bad request인지 판단
    private HTTPResponseDto checkLoginBadRequest(HTTPRequestDto httpRequestDto) {
        // 1. 이미 로그인 되어있을 경우
        if(httpRequestDto.getSessionId() != null)
            return new HTTPResponseDto(400, "text/plain", "이미 로그인 되어있습니다.".getBytes());
        // 2. request body가 null 일 경우
        if(httpRequestDto.getBody() == null)
            return new HTTPResponseDto(400, "text/plain", "Bad Request".getBytes());

        String userId = httpRequestDto.getBody().getValue("userId");
        String password = httpRequestDto.getBody().getValue("password");

        // 3. body에 userId, password 필드 네임이 없는 경우
        if(userId == null || password == null)
            return new HTTPResponseDto(400, "text/plain", "Bad Request".getBytes());

        // 4. 하나라도 빈 문자열이면 안됨
        if(userId.equals("") || password.equals(""))
            return new HTTPResponseDto(400, "text/plain", "모든 정보를 입력해주세요.".getBytes());

        return null;        // 적절한 요청
    }

    // 로그인 성공 시 세션 및 응답 처리
    private HTTPResponseDto loginSuccess(HTTPRequestDto httpRequestDto, String userId) {
        // 로그인 정보 처리 후 세션 반환
        Session session = sessionReturn(httpRequestDto.getSessionId(), userId);

        // http response 생성
        HTTPResponseDto httpResponseDto = new HTTPResponseDto("/index.html");

        // 쿠키는 여러 개일 수 있으므로 value에 전체 헤더를 저장
        String setCookie = "sid=" + session.getId() + "; expires=" + session.getExpiresWithFormat() + "; Path=/; secure; HttpOnly\r\n";
        httpResponseDto.setHeader("Set-Cookie", "Set-Cookie: " + setCookie);
        return httpResponseDto;
    }

    // 로그인 요청 유저가 기존의 세션을 가지는지 여부에 따라 세션을 이용하여 유저 정보 업데이트 후 세션 반환
    private Session sessionReturn(String sessionId, String userId) {
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
        return session;
    }

    // 게시글 생성 요청 처리
    public HTTPResponseDto createOrUpdatePost(HTTPRequestDto httpRequestDto) {
        String sessionId = httpRequestDto.getSessionId();
        User user = Database.findUserBySessionId(sessionId);
        HTTPResponseDto httpResponseDto = checkCreatePostBadRequest(httpRequestDto, user);
        if(httpResponseDto != null)         // 400 bad request일 경우
            return httpResponseDto;

        String writer = httpRequestDto.getBody().getValue("writer");
        String title = httpRequestDto.getBody().getValue("title");
        String contents = httpRequestDto.getBody().getValue("contents");

        // 글쓴이가 비어있을 경우 -> 유저의 이름 자동으로 넣어주기
        if(writer == null || writer.equals(""))
            writer = user.getName();

        // 게시물 생성
        Post post = makePost(httpRequestDto.getRequestTarget(), user.getUserId(), writer, title, contents);
        // 게시물 저장
        Database.addPost(post);

        // /index.html로 리다이렉트
        return new HTTPResponseDto("/");
    }
    private HTTPResponseDto checkCreatePostBadRequest(HTTPRequestDto httpRequestDto, User user) {
        // body 또는 유저가 null일 경우
        if(httpRequestDto.getBody() == null || user == null)
            return new HTTPResponseDto(400, "text/plain", "Bad Request".getBytes());
        // body에 title, contents가 하나라도 들어있지 않은 경우
        FirstClassCollection body = httpRequestDto.getBody();
        String title = body.getValue("title");
        String contents = body.getValue("contents");
        if(title == null || title.isEmpty() || contents == null || contents.isEmpty())
            return new HTTPResponseDto(400, "text/plain", "제목과 내용을 모두 입력해주세요.".getBytes());

        return null;
    }
    // 게시물 생성 또는 수정하여 반환
    private Post makePost(String url, String userId, String writer, String title, String contents) {
        try {
            String postId = url.substring(url.indexOf("form.html/") + "form.html/".length());
            return new Post(Long.parseLong(postId), userId, writer, title, contents);
        } catch(NumberFormatException e) {
            return new Post(userId, writer, title, contents);       // 새로운 게시글 생성
        }
    }
}
