package webserver;

import http.HttpStatus;
import http.request.HttpRequest;
import http.response.HttpResponse;
import model.User;
import utils.FileReader;
import utils.Parser;
import utils.SessionManager;

import java.io.File;

public class ResourceHandler {
    public static HttpResponse process(String filePath, HttpRequest request) {
        File file = new File(filePath);
        // 파일 존재하지 않거나 찾은 경로가 디렉토리일 경우 NOT_FOUND 반환
        if (!file.exists() || file.isDirectory()) {
            return HttpResponse.of(HttpStatus.NOT_FOUND);
        }

        // 파일 존재할 경우 해당 파일 읽어옴
        byte[] body = FileReader.readFile(file);

        // 세션 아이디 파싱
        String cookie = request.getEtcHeaders().getOrDefault("Cookie", "");
        if (!cookie.isEmpty() && filePath.contains(".html")) {
            User user = SessionManager.findUserBySessionId(Parser.extractSid(cookie));
            if (user != null) {
                String bodyAsString = new String(body);
                body = bodyAsString.replace("로그인", user.getName()).getBytes();
            }
        }
        // Request 헤더의 Accept 필드를 참고하여 반환할 타입 지정
        String contentType = request.getEtcHeaders().get("Accept").split(",")[0];
        return HttpResponse.of(HttpStatus.OK, contentType, body);
    }
}
