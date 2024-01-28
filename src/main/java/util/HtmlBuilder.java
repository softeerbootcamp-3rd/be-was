package util;

import model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HtmlBuilder {

    public static String replace(String key, Object value) {
        if (key.equals("{{user-list}}")) {
            List<User> users = new ArrayList<>((Collection<User>) value);
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < users.size(); i++) {
                User user = users.get(i);
                sb.append("<tr>");
                sb.append("<th scope=\"row\">" + i + 1 + "</th>");
                sb.append("<td>" + user.getUserId() + "</td>");
                sb.append("<td>" + user.getName() + "</td>");
                sb.append("<td>" + user.getEmail() + "</td>");
                sb.append("<td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>");
                sb.append("</tr>");
            }
            return sb.toString();

        }
        throw new IllegalArgumentException("해당하는 html template이 존재하지 않습니다.");
    }

    public static String empty() {
        return "";
    }
}
