package content;

import model.Model;

public enum FileContent {
    NON_LOGIN("<li><a href=\"../user/form.html\" role=\"button\">회원가입</a></li>\n" +
            "<li><a href=\"../user/login.html\" role=\"button\">로그인</a></li>"),
    LOGIN("<li><a href=\"#\" role=\"button\">로그아웃</a></li>\n" +
            "<li><a href=\"#\" role=\"button\">%s 님</a></li>\n" +
            "<li><a href=\"#\" role=\"button\">개인정보 수정</a></li>\n");

    private String text;
    FileContent (String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public String getText(String username) {
        return String.format(text, username);
    }
}
