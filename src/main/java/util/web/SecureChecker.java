package util.web;

import constant.SecurePath;
import webserver.HttpRequest;

import java.util.regex.Pattern;

public class SecureChecker {

    public static String checkRedirect(HttpRequest request) {
        for (SecurePath securePath : SecurePath.values()) {
            if (Pattern.matches(securePath.getPattern(), request.getPath())
                    && !securePath.getChecker().get())
                return securePath.getRedirectPath();
        }
        return "";
    }

    public static Boolean isLoggedIn() {
        return SharedData.requestUser.get() != null;
    }
}
