package util;

import data.RequestData;
import db.Database;
import db.Session;
import model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DynamicHtml {
    public static String modifyHtml(String originalHtml, boolean isLoggedIn, RequestData requestData) {
        StringBuilder modifiedHtml = new StringBuilder(originalHtml);

        String name = Database.findUserById(Session.getUserIdBySessionId(RequestParserUtil.parseUserRegisterQuery(requestData.getHeaderValue("Cookie")).get("sid"))).getName();

        // 예시: 로그인 상태에 따라 동적으로 변경할 부분
        Map<String, String> dynamicContent = new HashMap<>();
        dynamicContent.put("<li><a href=\"user/login.html\" role=\"button\">로그인</a></li>", ""); // `/index.html`의 로그인 버튼
        dynamicContent.put("<li><a href=\"../user/login.html\" role=\"button\">로그인</a></li>", ""); // `/user/*.html`의 로그인 버튼
        dynamicContent.put("<li><a href=\"./user/list.html\"><i class=\"glyphicon glyphicon-user\"></i></a></li>", "<li class=\"navbar-brand\">" + name + "님 안녕하세요</li>\r\n<li><a href=\"./user/list.html\"><i class=\"glyphicon glyphicon-user\"></i></a></li>"); // `/index.html`의 로그인 버튼
        dynamicContent.put("<li><a href=\"../user/list.html\"><i class=\"glyphicon glyphicon-user\"></i></a></li>", "<li class=\"navbar-brand\">" + name + "님 안녕하세요</li>\r\n<li><a href=\"../user/list.html\"><i class=\"glyphicon glyphicon-user\"></i></a></li>"); // `/user/*.html`의 로그인 버튼

        // 사용자 리스트 동적 변경
        Collection<User> allUsers = Database.findAll();

        StringBuilder userListHtml = new StringBuilder();
        for (User user : allUsers) {
            userListHtml.append("<tr>\n")
                    .append("    <th scope=\"row\">").append(user.getUserId()).append("</th>")
                    .append("    <td>").append(user.getName()).append("</td>")
                    .append("    <td>").append(user.getEmail()).append("</td>")
                    .append("    <td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>\n")
                    .append("</tr>\n");
        }

        dynamicContent.put("<tr>\n" +
                "                    <th scope=\"row\">1</th> <td>javajigi</td> <td>자바지기</td> <td>javajigi@sample.net</td><td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <th scope=\"row\">2</th> <td>slipp</td> <td>슬립</td> <td>slipp@sample.net</td><td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>\n" +
                "                </tr>", userListHtml.toString());

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
