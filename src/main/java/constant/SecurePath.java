package constant;

import util.web.SecureChecker;

import java.util.function.Supplier;

public enum SecurePath {
    POST_WRITE("^/post/write\\.html.*", SecureChecker::isLoggedIn, "/user/login.html"),
    POST_SHOW("^/post/show\\.html.*", SecureChecker::isLoggedIn, "/user/login.html"),
    ATTACHMENT("^/attachment.*", SecureChecker::isLoggedIn, "/user/login.html"),
    ;

    private final String pattern;
    private final Supplier<Boolean> checker;
    private final String redirectPath;

    SecurePath(String pattern, Supplier<Boolean> checker, String redirectPath) {
        this.pattern = pattern;
        this.checker = checker;
        this.redirectPath = redirectPath;
    }

    public String getPattern() {
        return pattern;
    }

    public Supplier<Boolean> getChecker() {
        return checker;
    }

    public String getRedirectPath() {
        return redirectPath;
    }
}
