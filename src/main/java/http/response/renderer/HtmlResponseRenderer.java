package http.response.renderer;

import utils.FileUtils;
public class HtmlResponseRenderer {

    public static String renderHtmlWithUserName(String htmlContent, String userName) {
        // 환영 문구를 먼저 추가
        String userGreeting = "<li class=\"nav-item welcome-message\">안녕하세요, " + userName + "님!</li>\n";

        // "Posts" 버튼과 환영 문구의 순서를 변경
        htmlContent = htmlContent.replaceFirst(
                "<ul class=\"nav navbar-nav navbar-right\">\\s*<li class=\"active\"><a href=\"index.html\">Posts</a></li>",
                "<ul class=\"nav navbar-nav navbar-right\">" +
                        userGreeting + "<li class=\"active\"><a href=\"index.html\">Posts</a></li>"
        );

        // 로그인/회원가입 버튼을 삭제
        htmlContent = htmlContent.replaceAll(
                        "<li><a href=\"user/login.html\" role=\"button\">로그인</a></li>", "")
                .replaceAll("<li><a href=\"user/form.html\" role=\"button\">회원가입</a></li>", "");

        return htmlContent;
    }

    public static String renderHtmlForLoggedOutState(String htmlContent) {
        // 환영 문구가 있는 경우에만 제거
        if (htmlContent.contains("nav-item welcome-message")) {
            htmlContent = htmlContent.replaceAll("<li class=\"nav-item welcome-message\">안녕하세요, .*님!</li>\\n", "");
        }

        // 로그인/회원가입 버튼이 이미 있는지 확인
        if (!htmlContent.contains("user/login.html") && !htmlContent.contains("user/form.html")) {
            // 로그인/회원가입 버튼 추가
            String loginSignUpButtons = "<li><a href=\"user/login.html\" role=\"button\">로그인</a></li>"
                    + "<li><a href=\"user/form.html\" role=\"button\">회원가입</a></li>";

            // "Posts" 버튼 옆에 로그인/회원가입 버튼 추가
            htmlContent = htmlContent.replaceFirst(
                    "<ul class=\"nav navbar-nav navbar-right\">\\s*<li class=\"active\"><a href=\"index.html\">Posts</a></li>",
                    "<ul class=\"nav navbar-nav navbar-right\">" +
                            "<li class=\"active\"><a href=\"index.html\">Posts</a></li>" + loginSignUpButtons
            );
        }

        return htmlContent;
    }
}
