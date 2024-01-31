package utils;

import model.User;

import java.io.IOException;
import java.util.List;

public class HtmlBuilder {
    public static String userListHtml(String htmlTemplate, List<User> users) {
        // 로그인 유저 목록
        StringBuilder userListHtml = new StringBuilder();
        int index = 1;
        for (User user : users) {
            userListHtml.append("<tr>")
                    .append("<th scope=\"row\">").append(index++).append("</th>")
                    .append("<td>").append(user.getUserId()).append("</td>")
                    .append("<td>").append(user.getName()).append("</td>")
                    .append("<td>").append(user.getEmail()).append("</td>")
                    .append("<td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>")
                    .append("</tr>");
        }
        htmlTemplate = htmlTemplate.replace("<!-- UserList -->", userListHtml);

        return htmlTemplate;
    }

    public static String loggedInNavBarHtml(String path, String userName) throws IOException {
        String htmlTemplate = ResourceReader.getInstance().getFileTemplate(path);

        // 로그인 유저이름 표시
        StringBuilder userNameHtml = new StringBuilder();
        userNameHtml.append("&nbsp&nbsp<strong>")
                .append(userName)
                .append("</strong>");
        htmlTemplate = htmlTemplate.replace("<!-- UserName -->", userNameHtml);

        // 로그인 상태의 네비게이션 바 조정
        StringBuilder navBarHtml = new StringBuilder();
        navBarHtml.append("<li class=\"active\"><a href=\"index.html\">Posts</a></li>")
                .append("<li><a href=\"#\" role=\"button\">로그아웃</a></li>")
                .append("<li><a href=\"#\" role=\"button\">개인정보수정</a></li>");
        htmlTemplate = htmlTemplate.replace("<!-- NavBar -->", navBarHtml);

        return htmlTemplate;
    }



    public static String guestNavBarHtml(String path) throws IOException {
        String htmlTemplate = ResourceReader.getInstance().getFileTemplate(path);

        // 미로그인 상태의 네비게이션 바 조정
        StringBuilder navBarHtml = new StringBuilder();
        navBarHtml.append("<li class=\"active\"><a href=\"index.html\">Posts</a></li>")
                .append("<li><a href=\"user/login.html\" role=\"button\">로그인</a></li>")
                .append("<li><a href=\"user/form.html\" role=\"button\">회원가입</a></li>");
        htmlTemplate = htmlTemplate.replace("<!-- NavBar -->", navBarHtml);

        return htmlTemplate;
    }
}
