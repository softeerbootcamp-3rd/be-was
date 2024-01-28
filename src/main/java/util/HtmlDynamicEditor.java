package util;

import db.Database;
import model.User;
import webserver.ThreadLocalManager;

import static constant.StaticFile.*;

public class HtmlDynamicEditor {

    public static byte[] edit(byte[] htmlByte, String path) {
        String htmlStr = new String(htmlByte);

        htmlStr = setNav(htmlStr);
        if (path.equals("/user/list.html")) {
            htmlStr = setUserList(htmlStr);
        }

        return htmlStr.getBytes();
    }

    public static String setNav(String htmlStr) {
        String nav = BEFORE_LOGIN_NAV;
        if (ThreadLocalManager.getSession() != null) {
            nav = AFTER_LOGIN_NAV;
        }
        return htmlStr.replace("{nav}", nav);
    }

    public static String setUserList(String htmlStr) {
        int i = 1;
        StringBuilder sb = new StringBuilder();
        for (User user : Database.findAll()) {
            sb.append(USER_INFO.replace("{i}", String.valueOf(i++))
                    .replace("{userId}", user.getUserId())
                    .replace("{userName}", user.getName())
                    .replace("{userEmail}", user.getEmail()));
        }
        return htmlStr.replace("{userList}", sb.toString());
    }

}
