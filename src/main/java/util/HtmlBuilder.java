package util;

import constant.HtmlContent;
import db.Database;
import model.User;
import webserver.HttpRequest;

public class HtmlBuilder {

    public static byte[] process(HttpRequest request, byte[] fileContent) {
        if (SessionManager.isLoggedIn(request)) {
            User user = SessionManager.getLoggedInUser(request);
            return new String(fileContent).replace("{{user-name}}", buildUserName(user))
                    .replace("{{login-btn}}", "")
                    .replace("{{user-list}}", buildUserList())
                    .getBytes();
        }

        return new String(fileContent).replace("{{user-name}}", "")
                .replace("{{login-btn}}", HtmlContent.LOGIN_BTN.getValue())
                .replace("{{user-list}}", "")
                .getBytes();
    }

    private static String buildUserName(User user) {
        String template = HtmlContent.USER_NAME.getValue();
        return template.replace("{{user-name}}", user.getName());
    }

    private static String buildUserList() {
        String template = HtmlContent.USER_LIST.getValue();
        StringBuilder sb = new StringBuilder();
        for (User user : Database.findAll()) {
            sb.append(template.replace("{{user-id}}", user.getUserId())
                    .replace("{{user-name}}", user.getName())
                    .replace("{{user-email}}", user.getEmail()));
        }
        return sb.toString();
    }
}
