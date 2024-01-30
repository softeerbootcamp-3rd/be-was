package util;

import db.PostDatabase;
import db.UserDatabase;
import dto.HttpRequestDto;
import model.Post;
import model.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class HtmlBuilder {
    // 로그인 여부에 따라 navBar를 변경한 /index.html 파일을 byte[]로 리턴
    public static byte[] buildIndexPage(HttpRequestDto request) {
        String html = readHtmlInString(WebUtil.getPath(request.getUri()));
        Map<String, String> replacements = new HashMap<>();
        replacements.put("{{user-navbar}}", buildIndexNavBar(request.getUser()));
        replacements.put("{{post-list}}", buildPostList());

        return replaceHtml(html, replacements);
    }

    // 로그인 여부에 따라 navBar를 변경한 /index.html 외의 templates 파일을 byte[]로 리턴
    public static byte[] buildPage(HttpRequestDto request, String html) {
        Map<String, String> replacements = new HashMap<>();
        replacements.put("{{user-navbar}}", buildNavBar(request.getUser()));

        return replaceHtml(html, replacements);
    }

    // User 목록 데이터를 넣은 /user/list.html 파일을 byte[]로 리턴
    public static byte[] buildUserListPage(HttpRequestDto request) {
        String html = readHtmlInString(WebUtil.getPath(request.getUri()));
        Map<String, String> replacements = new HashMap<>();
        replacements.put("{{user-navbar}}", buildNavBar(request.getUser()));
        replacements.put("{{user-list}}", buildUserList());

        return replaceHtml(html, replacements);
    }

    public static byte[] buildWriteFormPage(HttpRequestDto request) {
        String html = readHtmlInString(WebUtil.getPath(request.getUri()));
        Map<String, String> replacements = new HashMap<>();
        replacements.put("{{user-navbar}}", buildNavBar(request.getUser()));
        replacements.put("{{writer-name}}", request.getUser().getName());

        return replaceHtml(html, replacements);
    }

    public static byte[] buildWriteDetailPage(Post post, User user) {
        String html = readHtmlInString(WebUtil.getPath("/qna/show.html"));
        Map<String, String> replacements = new HashMap<>();
        replacements.put("{{user-navbar}}", buildNavBar(user));
        replacements.put("{{post-title}}", post.getTitle());
        replacements.put("{{post-user-name}}", post.getWriterName());
        replacements.put("{{post-create-date}}", post.getCreateTime());
        replacements.put("{{post-contents}}", post.getContents());
        replacements.put("{{post-user-id}}", post.getUserId());
        replacements.put("{{post-image}}", buildPostImage(post));
        replacements.put("{{post-file}}", buildPostFile(post));

        return replaceHtml(html, replacements);
    }

    public static byte[] buildProfile(User user) {
        String html = readHtmlInString(WebUtil.getPath("/user/profile.html"));
        Map<String, String> replacements = new HashMap<>();
        replacements.put("{{user-navbar}}", buildNavBar(user));
        replacements.put("{{profile-user-name}}", user == null ? "존재하지 않는 유저입니다." : user.getName());
        replacements.put("{{profile-user-email}}", user == null ? "" : user.getEmail());

        return replaceHtml(html, replacements);
    }

    public static  byte[] buildErrorPage(String statusCode, String statusMessage, String message) {
        String html = readHtmlInString(WebUtil.getPath("/404.html"));
        Map<String, String> replacements = new HashMap<>();
        replacements.put("{{status-code}}", statusCode);
        replacements.put("{{status-message}}", statusMessage);
        replacements.put("{{error-message}}", message);

        return replaceHtml(html, replacements);
    }

    // 로그인 여부에 따라 /index.html의 navBar 코드 리턴
    private static String buildIndexNavBar(User user) {
        StringBuilder navBar = new StringBuilder();
        if (user == null) {
            // 로그인 하지 않은 경우
            navBar.append("<li><a href=\"/user/login.html\" role=\"button\">로그인</a></li>")
                    .append("<li><a href=\"/user/form.html\" role=\"button\">회원가입</a></li>");
        } else {
            // 로그인 한 경우
            navBar.append("<li><a href=\"/user/logout\" role=\"button\">로그아웃</a></li>")
                    .append("<li><a href=\"#\" role=\"button\">개인정보수정</a></li>")
                    .append("<li><a href=\"/user/profile.html\">").append(user.getName()).append("</a></li>");
        }
        return navBar.toString();
    }

    // 로그인 여부에 따라 /index.html 이외의 templates 파일의 navBar 코드 리턴
    private static String buildNavBar(User user) {
        StringBuilder navBar = new StringBuilder();
        if (user == null) {
            // 로그인하지 않은 경우
            navBar.append("<li><a href=\"../user/login.html\" role=\"button\">로그인</a></li>")
                    .append("<li><a href=\"../user/form.html\" role=\"button\">회원가입</a></li>");
        } else {
            // 로그인 한 경우
            navBar.append("<li><a href=\"../user/logout\" role=\"button\">로그아웃</a></li>")
                    .append("<li><a href=\"#\" role=\"button\">개인정보수정</a></li>")
                    .append("<li><a>").append(user.getName()).append("</a></li>");
        }
        return navBar.toString();
    }

    // User 목록 코드 리턴
    private static String buildUserList() {
        Collection<User> users = UserDatabase.findAll();
        StringBuilder userList = new StringBuilder();
        int index = 1;
        for (User user: users) {
            userList.append("<tr>")
                    .append("<th scope=\"row\">").append(index).append("</th>")
                    .append("<td>").append(user.getUserId()).append("</td>")
                    .append("<td>").append(user.getName()).append("</td>")
                    .append("<td>").append(user.getEmail()).append("</td>")
                    .append("<td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>")
                    .append("</tr>");
            index++;
        }
        return userList.toString();
    }

    private static String buildPostList() {
        Collection<Post> posts = PostDatabase.findAll();
        StringBuilder postList = new StringBuilder();
        for (Post post: posts) {
            postList.append("<li>")
                    .append("<div class=\"main\">")
                    .append("<strong class=\"subject\">")
                    .append("<a href=\"./qna/show?postId=").append(post.getPostId()).append("\">").append(post.getTitle()).append("</a>")
                    .append("</strong>")
                    .append("<div class=\"auth-info\">")
                    .append("<i class=\"icon-add-comment\"></i>\n")
                    .append("<span class=\"time\">").append(post.getCreateTime()).append("</span>")
                    .append("<a href=\"./user/profile?userId=").append(post.getUserId()).append("\" class=\"author\">").append(post.getWriterName()).append("</a>")
                    .append("</div>")
                    .append("<div class=\"reply\" title=\"댓글\">")
                    .append("<i class=\"icon-reply\"></i>")
                    .append("<span class=\"point\">8</span>")
                    .append("</div>")
                    .append("</li>");
        }
        return postList.toString();
    }

    private static String buildPostImage(Post post) {
        // 파일이 image인 경우
        if (post.getFile() != null && post.getFileContentType().startsWith("image")) {
            StringBuilder image = new StringBuilder();
            String encodedFile = Base64.getEncoder().encodeToString(post.getFile());
            return image.append("<img src=\"data:")
                    .append(post.getFileContentType())
                    .append(";base64,")
                    .append(encodedFile)
                    .append("\" alt=\"File\"").toString();
        }
        return "";
    }

    private static String buildPostFile(Post post) {
        if (post.getFile() != null) {
            StringBuilder postFile = new StringBuilder();

            return postFile.append("<li>")
                    .append("<a class=\"link-modify-article\" href=\"")
                    .append("/download?postId=").append(post.getPostId()).append("\">")
                    .append("(Download) ").append(post.getFileName())
                    .append("</a>")
                    .append("</li>").toString();
        }
        return "";
    }

    private static byte[] replaceHtml(String html, Map<String, String> replaces) {
        StringBuilder page = new StringBuilder(html);

        for (Map.Entry<String, String> entry : replaces.entrySet()) {
            replaceAllOccurrences(page, entry.getKey(), entry.getValue());
        }
        return page.toString().getBytes();
    }

    private static void replaceAllOccurrences(StringBuilder builder, String target, String replacement) {
        int index = builder.indexOf(target);
        while (index != -1) {
            builder.replace(index, index + target.length(), replacement);
            index = builder.indexOf(target, index + replacement.length());
        }
    }

    private static String readHtmlInString(String path) {
        try {
            return new String(Files.readAllBytes(new File(path).toPath()));
        } catch (IOException e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }
}
