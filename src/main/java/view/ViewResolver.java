package view;

import db.Database;
import model.Post;
import model.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ViewResolver {

    public static byte[] resolve(String url, Boolean isAuth, Map<String, Object> model) {
        String originalUrl = url;
        if (url.endsWith(".html")) {
            url = "src/main/resources/templates" + url;
        } else {
            url = "src/main/resources/static" + url;
        }

        File file = new File(url);
        if (file.isFile()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                byte[] body = fis.readAllBytes();
                if (url.endsWith(".html")) {
                    String content = new String(body, "UTF-8");
                    content = replaceTag(content, "<ul class=\"list\"></ul>", getContentListHtml(Database.getPostList()));
                    if(isAuth) {
                        content = replaceTag(content, "로그인", (String)model.get("name"));

                        if (originalUrl.equals("/user/list.html")) {
                            content = replaceTag(content, "<tbody></tbody>", makeListHtml(Database.findAll()));
                        }

                        if (originalUrl.equals("/post_detail.html")) {
                            content = replaceTag(content, "<div class=\"panel panel-default\"><div class=\"article-utils\">", getPostDetailHtml((String)model.get("postId")));
                        }
                    }



                    body = content.getBytes("UTF-8");
                }
                return body;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            return null;
        }

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

    private static String getContentListHtml(Map<Long, Post> postList) {
        StringBuilder sb = new StringBuilder();
        sb.append("<ul class=\"list\">");
        for (Post post : postList.values()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String createTime = post.getCreateTime().format(formatter);

            sb.append("<li>");
            sb.append("<div class=\"wrap\">");
            sb.append("<div class=\"main\">");
            sb.append("<strong class=\"subject\">");
            sb.append("<a href=\"./post/detail?id=" + post.getId() + "\">" + post.getTitle() + "</a>");
            sb.append("</strong>");
            sb.append("<div class=\"auth-info\">");
            sb.append("<i class=\"icon-add-comment\"></i>");
            sb.append("<span class=\"time\">"+createTime+"</span>");
            sb.append("<a href=\"./user/profile.html\" class=\"author\">"+ post.getWriter() +"</a>");
            sb.append("</div>");
            sb.append("<div class=\"reply\" title=\"댓글\">");
            sb.append("<i class=\"icon-reply\"></i>");
            sb.append("<span class=\"point\">0</span>");
            sb.append("</div>");
            sb.append("</div>");
            sb.append("</div>");
            sb.append("</li>");
        }
        sb.append("</ul>");
        return sb.toString();
    }

    private static String makeListHtml(Collection<User> users) {
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

    public static String getPostDetailHtml(String id) {
        Long postId = Long.parseLong(id);
        Post post = Database.getPostById(postId);

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
