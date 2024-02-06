package utils;

import model.User;
import java.util.List;

public class UserHtmlConverter {

    public static String convertUsersToHtml(List<User> users) {
        StringBuilder htmlBuilder = new StringBuilder();

        int counter = 1;
        for (User user : users) {
            htmlBuilder.append("<tr>");
            htmlBuilder.append("<th scope=\"row\">").append(counter).append("</th>");
            htmlBuilder.append("<td>").append(user.getUserId()).append("</td>");
            htmlBuilder.append("<td>").append(user.getName()).append("</td>");
            htmlBuilder.append("<td>").append(user.getEmail()).append("</td>");
            htmlBuilder.append("<td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>");
            htmlBuilder.append("</tr>");
            counter++;
        }

        return htmlBuilder.toString();
    }
}
