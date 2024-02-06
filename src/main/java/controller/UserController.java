package controller;

import http.annotation.GetMapping;
import http.annotation.PostMapping;
import http.request.HttpRequest;
import http.response.HttpResponse;
import model.User;
import service.UserService;
import http.session.SessionStorage;
import utils.FileUtils;
import utils.UserHtmlConverter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class UserController {

    private final UserService userService;

    public UserController() {
        this.userService = UserService.getInstance();
    }

    @PostMapping("/user/create")
    public void createUser(HttpRequest request, HttpResponse response) {
        // POST 요청 본문 데이터를 사용하여 User 객체 생성
        Map<String, String> formData = request.getBodyParams();
        String userId = formData.get("userId");
        String password = formData.get("password");
        String name = formData.get("name");
        String email = formData.get("email");

        User user = userService.registerUser(userId, password, name, email);

        // 회원가입 성공 후 리디렉션 응답 설정
        response.setStatusCode(302);
        response.setHeader("Location", "/index.html");
    }

    @PostMapping("/user/login")
    public void login(HttpRequest request, HttpResponse response) {
        String userId = request.getBodyParams().get("userId");
        String password = request.getBodyParams().get("password");

        String sessionId = userService.loginUser(userId, password);

        if (sessionId != null) {
            User user = userService.getUserById(userId); // UserService를 사용하여 사용자 정보 가져오기
            SessionStorage sessionStorage = SessionStorage.getInstance();
            sessionStorage.addSession(sessionId, user);

            response.setHeader("Set-Cookie", "sid=" + sessionId + "; Path=/; HttpOnly");
            response.setStatusCode(302);
            response.setHeader("Location", "/index.html");
        } else {
            response.setStatusCode(302);
            response.setHeader("Location", "/user/login_failed.html");
        }
    }

    @GetMapping("/user/logout")
    public void logout(HttpRequest request, HttpResponse response) {
        String sessionId = request.getSessionId();
        if (sessionId != null) {
            SessionStorage.getInstance().removeSession(sessionId);
            response.setHeader("Set-Cookie", "sid=; Path=/; Max-Age=0");
        }
        response.setStatusCode(302);
        response.setHeader("Location", "/index.html");
    }

    @GetMapping("/user/list")
    public void listUsers(HttpRequest request, HttpResponse response) {
        String sessionId = request.getSessionId();
        if (sessionId == null || SessionStorage.getInstance().getUserBySessionId(sessionId) == null) {
            // 로그인하지 않은 경우 로그인 페이지로 리디렉션
            response.setStatusCode(302);
            response.setHeader("Location", "/user/login.html");
        } else {
            try {
                // 로그인한 경우 사용자 목록 출력
                List<User> users = userService.getAllUsers();
                String userHtml = UserHtmlConverter.convertUsersToHtml(users);

                // 기존 HTML 템플릿 파일 로드
                String templateHtml = FileUtils.readFileAsString("templates/user/list.html");

                // 사용자 목록을 삽입할 위치 찾기
                String finalHtml = templateHtml.replace("<!-- UserList -->", userHtml);

                // 최종 HTML 문서를 응답으로 전송
                response.setBody(finalHtml.getBytes());
                response.setHeader("Content-Type", "text/html");
            } catch (IOException e) {
                // IOException 처리
                response.setStatusCode(500); // 내부 서버 오류
                response.setBody("Internal server error".getBytes());
            }
        }
    }
}