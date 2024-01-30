package util;

import model.Qna;
import model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HtmlBuilder {
    public static void dynamicRender(StringBuilder fileBuilder, String key, Object value) {
        String renderedHtml = "";

        // value가 비어있는지 확인
        if (value == "") {
            renderedHtml = "";
            replace(fileBuilder, key, renderedHtml);
            return;
        }
        if (key.equals("{{user-list}}")) {
            List<User> users = new ArrayList<>((Collection<User>) value);
            renderedHtml = buildUserListHtml(users);
        }
        if (key.equals("{{welcome}}")) {
            String username = (String) value;
            renderedHtml = buildWelcomeHtml(username);
        }
        if (key.equals("{{qna-list}}")) {
            List<Qna> qnas = new ArrayList<>((Collection<Qna>) value);
            renderedHtml = buildQnaListHtml(qnas);
        }

        // fileBuilder에서 key를 renderedHtml로 교체
        replace(fileBuilder, key, renderedHtml);
    }

    private static void replace(StringBuilder fileBuilder, String key, String renderedHtml) {
        int start = fileBuilder.indexOf(key);
        if (start >= 0) {
            fileBuilder.replace(start, start + key.length(), renderedHtml);
        }
    }

    private static String buildUserListHtml(List<User> users) {
        StringBuilder sb = new StringBuilder();
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

    private static String buildWelcomeHtml(String username) {
        StringBuilder sb = new StringBuilder();
        sb.append("<li> 안녕하세요! ");
        sb.append(username);
        sb.append("님.</li>");
        return sb.toString();
    }

    private static String buildQnaListHtml(List<Qna> qnas) {
        StringBuilder sb = new StringBuilder();
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

}
