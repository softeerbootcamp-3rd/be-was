package controller;

import annotation.GetMapping;
import annotation.PostMapping;
import annotation.RequestParam;
import db.Database;
import model.User;
import request.HttpRequest;
import response.HttpResponse;
import response.HttpResponseBuilder;
import response.HttpResponseStatus;
import service.UserJoinService;
import service.UserLoginService;
import session.SessionManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserController {
    Map<String, String> responseHeaders = new HashMap<>();
    final String LOCATION = "Location";
    final String CONTENT_TYPE = "Content-Type";
    final String CONTENT_LENGTH = "Content-Length";
    final String SET_COOKIE = "set-cookie";
    SessionManager sessionManager = new SessionManager();

    @PostMapping(url = "/user/create")
    public String userJoin(@RequestParam(name = "userId") String userId,
                                 @RequestParam(name = "password") String password,
                                 @RequestParam(name = "name") String name,
                                 @RequestParam(name = "email") String email) {
        UserJoinService userJoinService = new UserJoinService();

        if (userJoinService.createUser(userId, password, name, email)) { // 회원 가입 성공 시 홈으로
            return "/index.html";
        } else { // 회원 가입 실패 시 다시 회원 가입 창으로
            return "/user/form.html";
        }
    }

    @PostMapping(url = "/user/login")
    public HttpResponse userLogin(@RequestParam(name = "userId") String userId,
                                  @RequestParam(name = "password") String password) {
        UserLoginService userLoginService = new UserLoginService();

        User loginUser = userLoginService.login(userId, password);
        if (loginUser == null) { // 로그인 실패 시 로그인 실패 창으로
            responseHeaders.put(LOCATION, "/user/login_failed.html");
        } else { // 로그인 성공 시 홈으로
            String sessionId = sessionManager.createSession(loginUser);
            responseHeaders.put(LOCATION, "/index.html");
            responseHeaders.put(SET_COOKIE, "sid="+sessionId+"; Max-Age=300; Path=/");
        }
        return new HttpResponseBuilder().status(HttpResponseStatus.FOUND)
                .headers(responseHeaders).build();
    }

    @PostMapping(url = "/user/logout")
    public String userLogout(HttpRequest request) {
        sessionManager.deleteSession(request);
        return "index.html";
    }

    @GetMapping(url = "/user/list.html")
    public HttpResponse getList(HttpRequest request) {
        User loginUser = sessionManager.getUserBySessionId(request);
        if (loginUser == null) {
            responseHeaders.put(LOCATION, "/user/login.html");
            return new HttpResponseBuilder().status(HttpResponseStatus.FOUND)
                    .headers(responseHeaders).build();
        } else {
            Collection<User> users = Database.findAll();

            File file = new File("src/main/resources/templates/user/list.html");
            try(FileInputStream fis = new FileInputStream(file)) {
                byte[] body = fis.readAllBytes();

                String content = new String(body, "UTF-8");
                String replacedBody = replaceWord(content, "<tbody></tbody>", makeListHtml(users));
                body = replacedBody.getBytes("UTF-8");

                responseHeaders.put(CONTENT_TYPE, "text/html; charset=utf-8");
                responseHeaders.put(CONTENT_LENGTH, String.valueOf(body.length));
                return new HttpResponseBuilder().status(HttpResponseStatus.OK)
                        .headers(responseHeaders)
                        .body(body)
                        .build();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private String makeListHtml(Collection<User> users) {
        StringBuilder sb = new StringBuilder();
        int sequence = 0;
        sb.append("<tbody>");
        for (User user : users) {
            sb.append("<tr>");
            sb.append("<th scope=\"row\">");
            sb.append(++sequence);
            sb.append("</th> <td>");
            sb.append(user.getUserId());
            sb.append("</th> <td>");
            sb.append(user.getName());
            sb.append("</th> <td>");
            sb.append(user.getEmail());
            sb.append("</td><td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>");
            sb.append("</tr>");
        }
        sb.append("</tbody>");
        return sb.toString();
    }

    private static String replaceWord(String content, String targetWord, String replacement) {
        // 정규 표현식 패턴 생성
        String regex = "\\Q" + targetWord + "\\E";  // 정규 표현식 특수 문자를 이스케이프
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);

        // 패턴과 일치하는 단어 찾기
        Matcher matcher = pattern.matcher(content);

        // 대체된 내용으로 문자열 빌드
        StringBuffer result = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(result);

        return result.toString();
    }
}
