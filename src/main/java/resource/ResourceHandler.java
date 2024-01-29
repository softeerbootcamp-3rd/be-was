package webserver;

import db.Database;
import http.HttpStatus;
import http.request.HttpRequest;
import http.response.HttpResponse;
import model.Post;
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
            bodyAsString = bodyAsString.replace("<!--user name-->", user.getName());
            // 유저 목록을 출력해야 할 경우 유저 목록 출력
            bodyAsString = bodyAsString.replace("<!--user List-->", generateDynamicUserList());
            // 포스트 목록을 출력해야 할 경우 글 목록 출력
            bodyAsString = bodyAsString.replace("<!--Post List-->", generateDynamicPostList());
            // 게시글 세부 내용 출력
//            bodyAsString = bodyAsString.replace("<!--Post Info-->", generateDynamicPostInfo(request.getParams()));
            // 프로필 정보를 출력해야 할 경우 프로필 정보 출력
            bodyAsString = bodyAsString.replace("<!--Profile Info-->", generateDynamicProfileInfo(user));

            body = bodyAsString.getBytes();
        } else if (filePath.contains("/user/list.html") || filePath.contains("/write.html")) {
            // 로그인이 안 되어있는 경우 유저 목록 접근 시 로그인 페이지로 redirection
            Map<String, String> header = Map.of("Location", "/user/login.html");
            return HttpResponse.of(HttpStatus.REDIRECT, header);
        }

        return HttpResponse.of(HttpStatus.OK, contentType, body);
    }

    private static String generateDynamicUserList() {
        Collection<User> userList = Database.findAllUsers();
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

    private static String generateDynamicPostList() {
        Collection<Post> posts = Database.findAllPosts();
        StringBuilder sb = new StringBuilder();

        for(Post post : posts) {
            sb.append("<li>\n")
                .append("<div class=\"wrap\">\n")
                .append("<div class=\"main\">\n")
                .append("<strong class=\"subject\">\n")
                .append("<a href=\"./qna/show.html?id=").append(post.getPostId()).append("\">").append(post.getTitle()).append("</a>\n")
                .append("</strong>\n")
                .append("<div class=\"auth-info\">\n")
                .append("<i class=\"icon-add-comment\"></i>\n")
                .append("<span class=\"time\">").append(post.getDate()).append("</span>\n")
                .append("<a href=\"./user/profile.html\" class=\"author\">").append(post.getWriter()).append("</a>\n")
                .append("</div>\n")
                .append("<div class=\"reply\" title=\"댓글\">")
                .append("<i class=\"icon-reply\"></i>")
                .append("<span class=\"point\">").append(post.getPostId()).append("</span>\n")
                .append("</div>\n")
                .append("</div>\n")
                .append("</div>\n")
                .append("</li>\n");

        }

        return sb.toString();
    }

    private static String generateDynamicPostInfo(Map<String, String> params) {
        Post post = Database.findPostById(params.get("id"));
        StringBuilder sb = new StringBuilder();

        sb.append("<header class=\"qna-header\">\n")
                .append("<h2 class=\"qna-title\">").append(post.getTitle()).append("</h2>\n")
                .append("/header>\n")
                .append("<div class=\"content-main\">\n")
                .append("<article class=\"article\">\n")
                .append("<div class=\"article-header\">\n")
                .append("<div class=\"article-header-thumb\">\n")
                .append("<img src=\"https://graph.facebook.com/v2.3/100000059371774/picture\" class=\"article-author-thumb\" alt=\"\">\n")
                .append("</div>\n")
                .append("<div class=\"article-header-text\">\n")
                .append("<a href=\"/users/92/kimmunsu\" class=\"article-author-name\">").append(post.getWriter()).append("</a>\n")
                .append("<a href=\"/questions/413\" class=\"article-header-time\" title=\"퍼머링크\">\n")
                .append(post.getDate())
                .append("\n<i class=\"icon-link\"></i>\n")
                .append("</a>\n")
                .append("</div>\n")
                .append("</div>\n")
                .append("<div class=\"article-doc\">\n")
                .append("<p>").append(post.getContents()).append("</p>\n")
                .append("</div>\n");

        return sb.toString();
    }

    private static String generateDynamicProfileInfo(User user) {
        StringBuilder sb = new StringBuilder();

        sb.append("<h4 class=\"media-heading\">").append(user.getName()).append("</h4>\n")
                .append("<p>\n")
                .append("<a href=\"#\" class=\"btn btn-xs btn-default\"><span class=\"glyphicon glyphicon-envelope\"></span>").append(user.getEmail()).append("</a>\n")
                .append("</p>\n");

        return sb.toString();
    }
}
