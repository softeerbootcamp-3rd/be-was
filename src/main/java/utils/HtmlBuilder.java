package utils;

import model.User;

import java.util.List;

public class HtmlBuilder {
    public static String userListHtml(List<User> users) {
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
        return userListHtml.toString();
    }

    public static String loggedInHomeHtml(String userName) {
        StringBuilder homeHtml = new StringBuilder();
        homeHtml.append("&nbsp&nbsp<strong>")
                .append(userName)
                .append("</strong>");
        return homeHtml.toString();
    }
}
