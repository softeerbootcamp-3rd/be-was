package utils;

import constants.Html;
import db.Database;
import java.util.Collection;
import model.User;

public class HtmlBuilder {

    public static String build(String url, byte[] body) {
        String bodyString = new String(body);
        if (url.startsWith("/user/list")) {
            bodyString = buildUserList(bodyString);
        }
        return bodyString;
    }

    private static String buildUserList(String bodyString) {
        Collection<User> users = Database.findAll();
        int count = 0;
        StringBuilder sb = new StringBuilder();
        for (User user : users) {
            count++;
            String userContentHtml = Html.USER_CONTENT.getText();
            String userContent = userContentHtml.replace("{{count}}", Integer.toString(count))
                    .replace("{{userId}}", user.getUserId())
                    .replace("{{userName}}", user.getName())
                    .replace("{{userEmail}}", user.getEmail());
            sb.append(userContent);
        }
        return bodyString.replace("{{userList}}",sb);
    }
}
