package util;

import java.util.HashMap;
import java.util.Map;

public class DynamicHtml {
    public static String modifyHtml(String originalHtml, boolean isLoggedIn) {
        StringBuilder modifiedHtml = new StringBuilder(originalHtml);

        // 예시: 로그인 상태에 따라 동적으로 변경할 부분
        Map<String, String> dynamicContent = new HashMap<>();
        dynamicContent.put("<li><a href=\"user/login.html\" role=\"button\">로그인</a></li>", ""); // `/index.html`의 로그인 버튼
        dynamicContent.put("<li><a href=\"../user/login.html\" role=\"button\">로그인</a></li>", ""); // `/user/*.html`의 로그인 버튼
        dynamicContent.put("<li><a href=\"./user/list.html\"><i class=\"glyphicon glyphicon-user\"></i></a></li>", "<li class=\"navbar-brand\">사용자님 안녕하세요</li>\r\n<li><a href=\"./user/list.html\"><i class=\"glyphicon glyphicon-user\"></i></a></li>"); // `/index.html`의 로그인 버튼
        dynamicContent.put("<li><a href=\"../user/list.html\"><i class=\"glyphicon glyphicon-user\"></i></a></li>", "<li class=\"navbar-brand\">사용자님 안녕하세요</li>\r\n<li><a href=\"../user/list.html\"><i class=\"glyphicon glyphicon-user\"></i></a></li>"); // `/user/*.html`의 로그인 버튼

        // 동적으로 HTML 수정
        for (Map.Entry<String, String> entry : dynamicContent.entrySet()) {
            String loggedOutText = entry.getKey();
            String loggedInText = entry.getValue();

            int index = modifiedHtml.indexOf(loggedOutText);
            if (index != -1) {
                modifiedHtml.replace(index, index + loggedOutText.length(), isLoggedIn ? loggedInText : loggedOutText);
            }
        }

        return modifiedHtml.toString();
    }
}
