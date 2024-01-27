package util;

import db.UserDatabase;
import dto.HttpRequestDto;
import model.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class HtmlBuilder {
    // 로그인 여부에 따라 navBar를 변경한 /index.html 파일을 byte[]로 리턴
    public static byte[] buildIndexPage(HttpRequestDto request) {
        String html = readHtmlInString(WebUtil.getPath(request.getUri()));
        Map<String, String> replacements = new HashMap<>();
        replacements.put("{{user-navbar}}", buildIndexNavBar(request.getUser()));

        return replaceHtml(html, replacements);
    }

    // 로그인 여부에 따라 navBar를 변경한 /index.html 외의 templates 파일을 byte[]로 리턴
    public static byte[] buildPage(HttpRequestDto request) {
        String html = readHtmlInString(WebUtil.getPath(request.getUri()));
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
                    .append("<li><a>").append(user.getName()).append("</a></li>");
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
            throw new RuntimeException(e);
        }
    }
}
