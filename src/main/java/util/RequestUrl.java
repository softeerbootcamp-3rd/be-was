package util;

public enum RequestUrl {
    HOME("/"),
    USER_FORM("/user/form"),
    USER_LIST("/user/list"),
    USER_LOGIN("/user/login"),
    USER_LOGIN_FAILED("/user/login_failed"),
    USER_PROFILE("/user/profile"),
    USER_CREATE("/user/create");

    private final String url;

    RequestUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
