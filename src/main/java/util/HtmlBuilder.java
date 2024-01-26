package util;

import constant.HtmlTemplate;
import db.UserDatabase;
import model.User;

import java.util.ArrayList;
import java.util.List;

public class HtmlBuilder {

    public static byte[] process(byte[] fileContent) {
        String result = new String(fileContent);
        User user = SharedData.requestUser.get();

        if (user != null) {
            for (HtmlTemplate template : HtmlTemplate.values()) {
                result = result.replace(template.getOriginalValue(),
                        template.getLoggedInFunction().apply(template.getTemplate()));
            }
            return result.getBytes();
        }

        for (HtmlTemplate template : HtmlTemplate.values()) {
            result = result.replace(template.getOriginalValue(),
                    template.getLoggedOutFunction().apply(template.getTemplate()));
        }
        return result.getBytes();
    }

    public static String empty(String unused) {
        return "";
    }

    public static String getRaw(String template) {
        if (template == null)
            return "";
        return template;
    }

    public static String replaceUser(String template) {
        if (template == null)
            return "";
        return template.replace("{{user-name}}", SharedData.requestUser.get().getName());
    }

    public static String replaceUserList(String template) {
        if (template == null)
            return "";
        StringBuilder sb = new StringBuilder();
        List<User> userList = new ArrayList<>(UserDatabase.findAll());
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
