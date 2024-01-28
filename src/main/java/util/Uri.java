package util;

public enum Uri {
    HOME("/"),
    HOME_INDEX("/index.html"),
    USER_FORM("/user/form.html"),
    USER_FORM_FAILED("/user/form_failed.html"),
    USER_LIST("/user/list.html"),
    USER_LOGIN("/user/login.html"),
    USER_LOGIN_FAILED("/user/login_failed.html"),
    USER_PROFILE("/user/profile.html"),
    USER_CREATE("/user/create");

    private final String uri;

    Uri(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }
}
