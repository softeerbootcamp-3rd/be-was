package util.html;

import db.UserDatabase;
import model.User;
import util.web.SharedData;

import java.util.ArrayList;
import java.util.List;

public class UserHtml {
    public static String replaceUser(String template) {
        if (template == null)
            return "";
        return template.replace("<!--user-name-->", SharedData.requestUser.get().getName());
    }

    public static String replaceUserList(String template) {
        if (template == null)
            return "";
        StringBuilder sb = new StringBuilder();
        List<User> userList = new ArrayList<>(UserDatabase.findAll());
        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            sb.append(template.replace("<!--order-->", String.valueOf(i + 1))
                    .replace("<!--user-id-->", user.getUserId())
                    .replace("<!--user-name-->", user.getName())
                    .replace("<!--user-email-->", user.getEmail()));
        }
        return sb.toString();
    }
}
