package util;

import db.Session;
import request.http.HttpRequest;

import static db.Session.findUserBySessionId;

public class AuthFilter {
    public static boolean isLogin(HttpRequest httpRequest) {
        String cookie = httpRequest.getRequestHeaders().getValue("Cookie");
        if (cookie == null) {
            return false;
        }
        for (String cookieValue : cookie.split(";")) {
            if (cookieValue.contains("sid") && findUserBySessionId(cookieValue.split("=")[1]) != null) {;
                return true;
            }
        }
        return false;
    }
}
