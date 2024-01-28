package model;

import httpmessage.HttpSession;
import httpmessage.request.HttpRequest;
import httpmessage.response.HttpResponse;

import java.util.Collection;

public class Content {
    private StringBuilder stringBuilder = new StringBuilder();

    public Content(HttpRequest httpRequest, HttpResponse httpResponse) {
        if (!httpRequest.getCookie().isEmpty()) {
            HttpSession httpSession = new HttpSession();
            String sid = httpRequest.getCookie();
            String userId = httpSession.getUser(sid).getUserId();

            stringBuilder
                    .append("<li>").append("<a href=\"#\" role=\"button\">").append(userId).append("</a>").append("</li>\n")
                    .append("<li>").append("<a href=\"#\" role=\"button\">").append("로그아웃").append("</a>").append("</li>\n")
                    .append("<li>").append("<a href=\"#\" role=\"button\">").append("개인정보수정").append("</a>").append("</li>\n");
        } else {
            stringBuilder
                    .append("<li>").append("<a href=\"user/login.html\" role=\"button\">").append("로그인").append("</a>").append("</li>\n");
        }
    }

    public Content(Collection<User> users){
        int index = 1;
        for (User user : users) {
            stringBuilder.append("<tr>\n")
                    .append("<th scope=\"row\">").append(index++).append("</th>")
                    .append("<td>").append(user.getUserId()).append("</td>")
                    .append("<td>").append(user.getName()).append("</td>")
                    .append("<td>").append(user.getEmail()).append("</td>")
                    .append("<td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>\n")
                    .append("</tr>\n");
        }
    }

    public String getString() {
        return stringBuilder.toString();
    }
}
