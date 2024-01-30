package controller;

import annotation.GetMapping;
import annotation.PostMapping;
import annotation.RequestParam;
import db.Database;
import model.Post;
import request.HttpRequest;
import response.HttpResponse;
import response.HttpResponseBuilder;
import response.HttpResponseStatus;
import session.SessionManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PostController {
    SessionManager sessionManager = new SessionManager();
    private static Long sequence = 0L;

    @GetMapping(url = "/post")
    public String getWriteForm(HttpRequest request) {
        if (sessionManager.getUserBySessionId(request) == null) {
            return "/user/login.html";
        } else {
            return "write_form.html";
        }
    }

    @PostMapping(url = "/post")
    public String createPost(@RequestParam(name = "writer") String writer,
                             @RequestParam(name = "title") String title,
                             @RequestParam(name = "contents") String contents) {
        Post post = new Post(writer, title, contents);
        post.setId(++sequence);
        post.setCreateTime(LocalDateTime.now());
        Database.addPost(post);
        return "/index.html";
    }

    @GetMapping(url = "/post/detail")
    public HttpResponse showDetail(HttpRequest request) {
        Map<String, String> responseHeaders = new HashMap<>();
        String path = "src/main/resources/templates/post_detail.html";
        File file = new File(path);
        if (file.isFile()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                byte[] body = fis.readAllBytes();
                if (sessionManager.getUserBySessionId(request) == null) {
                    responseHeaders.put("Location", "/user/login.html");
                    return new HttpResponseBuilder().status(HttpResponseStatus.FOUND)
                            .headers(responseHeaders).build();
                } else {
                    String id = request.getParams().get("id");
                    Long postId = Long.parseLong(id);
                    String content = new String(body, "UTF-8");
                    String replacedBody = replaceTag(content, "<div class=\"panel panel-default\"><div class=\"article-utils\">", getPostDetailHtml(postId));
                    body = replacedBody.getBytes("UTF-8");

                    responseHeaders.put("Content-Type", "text/html; charset=utf-8");
                    responseHeaders.put("Content-Length", String.valueOf(body.length));
                }
                return new HttpResponseBuilder().status(HttpResponseStatus.OK)
                        .headers(responseHeaders)
                        .body(body)
                        .build();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    private static String replaceTag(String content, String targetWord, String replacement) {
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

    public static String getPostDetailHtml(Long id) {
        Post post = Database.getPostById(id);

        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"panel panel-default\">");
        sb.append("<header class=\"qna-header\">\n");
        sb.append("<h2 class=\"qna-title\">" + post.getTitle() + "</h2>\n");
        sb.append("</header>\n");
        sb.append("<div class=\"content-main\">\n");
        sb.append("<article class=\"article\">\n");
        sb.append("<div class=\"article-header\">\n");
        sb.append("<div class=\"article-header-thumb\">\n");
        sb.append("<img src=\"https://graph.facebook.com/v2.3/100000059371774/picture\" class=\"article-author-thumb\" alt=\"\">\n");
        sb.append("</div>\n");
        sb.append("<div class=\"article-header-text\">\n");
        sb.append("<a href=\"/users/92/kimmunsu\" class=\"article-author-name\">" + post.getWriter() + "</a>\n");
        sb.append("<a href=\"/questions/413\" class=\"article-header-time\" title=\"퍼머링크\">\n");
        sb.append(post.getCreateTime() + "\n");
        sb.append("<i class=\"icon-link\"></i>\n");
        sb.append(" </a>\n");
        sb.append("</div>\n");
        sb.append("</div>\n");
        sb.append("<div class=\"article-doc\">\n");
        sb.append("<p>"+post.getContents()+"</p>\n");
        sb.append("</div>\n");
        sb.append("</article>\n");
        sb.append("</div>\n");
        sb.append("<div class=\"article-utils\">");

        return sb.toString();
    }
}
