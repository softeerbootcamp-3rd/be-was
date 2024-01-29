package util;

import db.Database;
import model.User;
import session.Session;
import webserver.ThreadLocalManager;

import static constant.FileConstant.*;

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
        Session session = ThreadLocalManager.getSession();
        String loginUserName = "";
        if (session != null) {
            nav = NICKNAME_NAV + AFTER_LOGIN_NAV;
            loginUserName = (String) session.getAttribute("userName");
        }
        return htmlStr.replace("{nav}", nav).replace("{userName}", loginUserName);
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
