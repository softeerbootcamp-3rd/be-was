package util;

import model.Qna;
import model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HtmlBuilder {

    public static String replace(String key, Object value) {
        StringBuilder sb = new StringBuilder();

        if (key.equals("{{user-list}}")) {
            List<User> users = new ArrayList<>((Collection<User>) value);

            for (int i = 0; i < users.size(); i++) {
                User user = users.get(i);
                sb.append("<tr>");
                sb.append("<th scope=\"row\">" + i + 1 + "</th>");
                sb.append("<td>" + user.getUserId() + "</td>");
                sb.append("<td>" + user.getName() + "</td>");
                sb.append("<td>" + user.getEmail() + "</td>");
                sb.append("<td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>");
                sb.append("</tr>");
            }
            return sb.toString();
        }

        if (key.equals("{{welcome}}")) {
            String username = (String) value;
            sb.append("<li> 안녕하세요! ");
            sb.append(username);
            sb.append("님.</li>");
            return sb.toString();
        }

        if (key.equals("{{qna-list}}")) {
            List<Qna> qnas = new ArrayList<>((Collection<Qna>) value);

            for (Qna qna : qnas) {
                String createdAt = qna.getCreatedAt().toString();
                String title = qna.getTitle();
                String writer = qna.getWriter();

                sb.append("<li>\n")
                        .append("  <div class=\"wrap\">\n")
                        .append("      <div class=\"main\">\n")
                        .append("          <strong class=\"subject\">\n")
                        .append("              <a href=\"./qna/show.html\">").append(title).append("</a>\n") // title 변수 적용
                        .append("          </strong>\n")
                        .append("          <div class=\"auth-info\">\n")
                        .append("              <i class=\"icon-add-comment\"></i>\n")
                        .append("              <span class=\"time\">").append(createdAt).append("</span>\n") // date 변수 적용
                        .append("              <a href=\"./user/profile.html\" class=\"author\">").append(writer).append("</a>\n") // writer 변수 적용
                        .append("          </div>\n")
                        .append("          <div class=\"reply\" title=\"댓글\">\n")
                        .append("              <i class=\"icon-reply\"></i>\n")
                        .append("              <span class=\"point\">0</span>\n")
                        .append("          </div>\n")
                        .append("      </div>\n")
                        .append("  </div>\n")
                        .append("</li>");
            }
            return sb.toString();
        }

        throw new IllegalArgumentException(key + "에 해당하는 html template이 존재하지 않습니다.");
    }

    public static String empty() {
        return "";
    }
}
