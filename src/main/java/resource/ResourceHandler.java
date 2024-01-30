package resource;

import http.HttpStatus;
import http.request.HttpRequest;
import http.response.HttpResponse;
import model.User;
import utils.FileReader;
import utils.SessionManager;

import java.io.File;
import java.util.Map;

public class ResourceHandler {
    public static HttpResponse process(String filePath, HttpRequest request) {
        File file = new File(filePath);
        // 파일 존재하지 않거나 찾은 경로가 디렉토리일 경우 NOT_FOUND 페이지로 리다이렉션
        if (!file.exists() || file.isDirectory()) {
            Map<String, String> header = Map.of("Location", "/404.html");
            return HttpResponse.of(HttpStatus.REDIRECT, header);
        }

        // 파일 존재할 경우 해당 파일 읽어옴
        byte[] body = FileReader.readFile(file);

        // Request 헤더의 Accept 필드를 참고하여 반환할 타입 지정
        String contentType = request.getEtcHeaderValue("Accept").split(",")[0];

        // 동적 HTML 렌더링
        String bodyAsString = new String(body);

        // 세션 아이디 파싱 및 검증
        // SID에 해당하는 유저가 있으면서 html 파일일 경우
        User user = SessionManager.findUserBySessionId(request.getCookie("sid"));
        if (user != null && filePath.contains(".html")) {
            // 로그인을 유저 이름으로 변경
            bodyAsString = bodyAsString.replace("<!--user name-->", user.getName());

            // 로그인 했을 때 렌더링 될 nav bar
            bodyAsString = bodyAsString.replace("<!--User Nav Bar-->", HtmlBuilder.generateUserNavBarInUser());
            bodyAsString = bodyAsString.replace("<!--User Nav Bar ROOT-->", HtmlBuilder.generateUserNavBarInROOT());

            // 유저 목록을 출력해야 할 경우 유저 목록 출력
            bodyAsString = bodyAsString.replace("<!--user List-->", HtmlBuilder.generateUserList());
            // 포스트 목록을 출력해야 할 경우 글 목록 출력
            bodyAsString = bodyAsString.replace("<!--Post List-->", HtmlBuilder.generatePostList());

            // 게시글 세부 내용 출력
            bodyAsString = bodyAsString.replace("<!--Post Info-->", HtmlBuilder.generatePostInfo(request.getParams()));
            // 게시글 세부 내용 중 첨부파일 출력
            bodyAsString = bodyAsString.replace("<!--Attached File Info-->", HtmlBuilder.generateAttachedFileInfo(request.getParams()));

            // 프로필 정보를 출력해야 할 경우 프로필 정보 출력
            bodyAsString = bodyAsString.replace("<!--Profile Info-->", HtmlBuilder.generateProfileInfo(user));
            body = bodyAsString.getBytes();
        } else if (filePath.contains("/user/list.html") || filePath.contains("/write.html")) {
            // 로그인이 안 되어있는 경우 유저 목록 혹은 글쓰기 페이지 접근 시 로그인 페이지로 redirection
            Map<String, String> header = Map.of("Location", "/user/login.html");
            return HttpResponse.of(HttpStatus.REDIRECT, header);
        } else if (filePath.contains(".html")) {
            bodyAsString = bodyAsString.replace("<!--Basic Nav Bar-->", HtmlBuilder.generateBasicNavBarInUser());
            bodyAsString = bodyAsString.replace("<!--Basic Nav Bar ROOT-->", HtmlBuilder.generateBasicNavBarInROOT());

            // 포스트 목록을 출력해야 할 경우 글 목록 출력
            bodyAsString = bodyAsString.replace("<!--Post List-->", HtmlBuilder.generatePostList());
            body = bodyAsString.getBytes();
        }
        return HttpResponse.of(HttpStatus.OK, contentType, body);
    }
}
