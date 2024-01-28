package controller;

import annotation.GetMapping;
import db.Database;
import model.User;
import request.HttpRequest;
import response.HttpResponse;
import response.HttpResponseStatus;
import session.SessionManager;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MemberListController extends CrudController {
    SessionManager sessionManager = new SessionManager();

    @GetMapping(url = "/user/list.html")
    public void doGet(HttpRequest request, HttpResponse response) {
        User loginUser = sessionManager.getUserBySessionId(request);
        if (loginUser == null) {
            responseHeaders.put(LOCATION, "/user/login.html");
            response.setResponse(HttpResponseStatus.FOUND, null, responseHeaders);
        } else {
            Collection<User> users = Database.findAll();

            String url = "src/main/resources/templates/user/list.html";
            try {
                byte[] body = Files.readAllBytes(new File(url).toPath());

                String content = new String(body, StandardCharsets.UTF_8);
                String replacedBody = replaceWord(content, "<tbody></tbody>", makeListHtml(users));
                body = replacedBody.getBytes(StandardCharsets.UTF_8);

                responseHeaders.put(CONTENT_TYPE, "text/html; charset=utf-8");
                responseHeaders.put(CONTENT_LENGTH, String.valueOf(body.length));
                response.setResponse(HttpResponseStatus.OK, body, responseHeaders);
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
