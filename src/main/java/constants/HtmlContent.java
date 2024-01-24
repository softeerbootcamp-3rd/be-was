package constants;

public enum HtmlContent {

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
