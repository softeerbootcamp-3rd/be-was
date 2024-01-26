package util;

import constant.HtmlTemplate;
import db.Database;
import model.User;
import webserver.HttpRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HtmlBuilder {

    public static byte[] process(HttpRequest request, byte[] fileContent) {
        String result = new String(fileContent);

        if (SessionManager.isLoggedIn(request)) {
            User user = SessionManager.getLoggedInUser(request);

            for (HtmlTemplate template : HtmlTemplate.values()) {
                result = result.replace(template.getOriginalValue(),
                        template.getLoggedInFunction().apply(template.getTemplate(), user));
            }
            return result.getBytes();
        }

        for (HtmlTemplate template : HtmlTemplate.values()) {
            result = result.replace(template.getOriginalValue(),
                    template.getLoggedOutFunction().apply(template.getTemplate(), User.empty()));
        }
        return result.getBytes();
    }

    public static String empty(String unusedString, User unusedUser) {
        return "";
    }

    public static String replaceOne(String template, User loggedInUser) {
        if (template == null)
            return "";
        return template.replace("{{user-name}}", loggedInUser.getName());
    }

    public static String replaceList(String template, User unusedUser) {
        if (template == null)
            return "";
        StringBuilder sb = new StringBuilder();
        List<User> userList = new ArrayList<>(Database.findAll());
        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            sb.append(template.replace("{{order}}", String.valueOf(i + 1))
                    .replace("{{user-id}}", user.getUserId())
                    .replace("{{user-name}}", user.getName())
                    .replace("{{user-email}}", user.getEmail()));
        }
        return sb.toString();
    }
}
