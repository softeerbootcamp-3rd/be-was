package webserver;

import db.Database;
import http.HttpStatus;
import http.request.HttpRequest;
import http.response.HttpResponse;
import model.User;
import utils.FileReader;
import utils.Parser;
import utils.SessionManager;

import java.io.File;
import java.util.Collection;
import java.util.Map;

public class ResourceHandler {
    public static HttpResponse process(String filePath, HttpRequest request) {
        File file = new File(filePath);
        // 파일 존재하지 않거나 찾은 경로가 디렉토리일 경우 NOT_FOUND 반환
        if (!file.exists() || file.isDirectory()) {
            return HttpResponse.of(HttpStatus.NOT_FOUND);
        }

        // 파일 존재할 경우 해당 파일 읽어옴
        byte[] body = FileReader.readFile(file);

        // Request 헤더의 Accept 필드를 참고하여 반환할 타입 지정
        String contentType = request.getEtcHeaderValue("Accept").split(",")[0];

        // 세션 아이디 파싱
        String cookie = request.getEtcHeaderValue("Cookie");

        // 세션 아이디 검증
        // SID에 해당하는 유저가 있으면서 html 파일일 경우
        User user = SessionManager.findUserBySessionId(Parser.extractSid(cookie));
        if (user != null && filePath.contains(".html")) {
            String bodyAsString = new String(body);
            // 로그인을 유저 이름으로 변경
            bodyAsString = bodyAsString.replace("로그인", user.getName());
            // 유저 목록을 출력해야 할 경우 유저 목록 출력
            body = bodyAsString.replace("<!--user List-->", generateDynamicUserList()).getBytes();
        } else if (filePath.contains("/user/list.html")) {
            // 로그인이 안 되어있는 경우 유저 목록 접근 시 로그인 페이지로 redirection
            Map<String, String> header = Map.of("Location", "/user/login.html");
            return HttpResponse.of(HttpStatus.REDIRECT, header);
        }

        return HttpResponse.of(HttpStatus.OK, contentType, body);
    }

    private static String generateDynamicUserList() {
        Collection<User> userList = Database.findAll();
        StringBuilder sb = new StringBuilder();
        int cnt = 0;
        for (User user : userList) {
            cnt++;
            sb.append("<tr>\n").append("<th scope=\"row\">").append(cnt).append("</th>")
                    .append("<td>").append(user.getUserId()).append("</td>")
                    .append("<td>").append(user.getName()).append("</td>")
                    .append("<td>").append(user.getEmail()).append("</td>")
                    .append("<td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>")
                    .append("</tr>");
        }

        return sb.toString();
    }
}
