package constants;

public enum HtmlContent {

    NAV_LOGOUT("<li><a href=\"{{location-login}}\" role=\"button\">로그인</a></li>"
            + "<li><a href=\"{{location-join}}\" role=\"button\">회원가입</a></li>\n"),
    NAV_LOGIN("<li><a href=\"#\" role=\"button\">{{userName}}</a></li>\n"
            + "<li><a href=\"#\" role=\"button\">로그아웃</a></li>\n"
            + "<li><a href=\"#\" role=\"button\">개인정보수정</a></li>"),
    USER_CONTENT("<tr>\n"
            + "<th scope=\"row\">{{count}}</th> "
            + "<td>{{userId}}</td> "
            + "<td>{{userName}}</td> "
            + "<td>{{userEmail}}</td>"
            + "<td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>\n"
            + "</tr>\n");

    private final String text;

    HtmlContent(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
