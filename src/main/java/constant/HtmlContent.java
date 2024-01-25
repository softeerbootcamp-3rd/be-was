package constant;

public enum HtmlContent {
    LOGIN_BTN("<li><a href=\"user/login.html\" role=\"button\">로그인</a></li>"),
    LOGOUT_BTN("<li><a href=\"/user/logout\" role=\"button\">로그아웃</a></li>"),
    SIGNUP_BTN("<li><a href=\"../user/form.html\" role=\"button\">회원가입</a></li>"),
    USER_NAME("<li><a id=\"userName\">{{user-name}}</a></li>"),
    USER_LIST("<tr>" +
            "   <th scope=\"row\">1</th>" +
            "   <td>{{user-id}}</td>" +
            "   <td>{{user-name}}</td>" +
            "   <td>{{user-email}}</td>" +
            "   <td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>" +
        "   </tr>");

    private final String value;

    HtmlContent(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
