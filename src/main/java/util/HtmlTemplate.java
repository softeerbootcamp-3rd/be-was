package util;

import model.Post;
import model.User;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HtmlTemplate {
    public static final String USER_ID = "<li><a role=\"button\">{}</a></li>";
    public static final String LOGOUT = "<li><a href=\"/user/logout\" role=\"button\">로그아웃</a></li>";
    public static final String POST = "           <li>\n" +
            "                  <div class=\"wrap\">\n" +
            "                      <div class=\"main\">\n" +
            "                          <strong class=\"subject\">\n" +
            "                              <a href=\"board/show.html\">{{title}}</a>\n" +
            "                          </strong>\n" +
            "                          <div class=\"auth-info\">\n" +
            "                              <i class=\"icon-add-comment\"></i>\n" +
            "                              <span class=\"time\">{{created}}</span>\n" +
            "                              <a href=\"./user/profile\" class=\"author\">{{writer}}</a>\n" +
            "                          </div>\n" +
            "                          <div class=\"reply\" title=\"댓글\">\n" +
            "                              <i class=\"icon-reply\"></i>\n" +
            "                              <span class=\"point\">0</span>\n" +
            "                          </div>\n" +
            "                      </div>\n" +
            "                  </div>\n" +
            "              </li>";

    public static byte[] index(User loggedInUser, List<Post> posts) throws IOException {
        StringBuilder sb = new StringBuilder();

        String html = new String(ResourceUtils.getStaticResource("/index.html"));
        for (Post p : posts) {
            sb.append(POST.replace("{{title}}", p.getTitle())
                    .replace("{{created}}", p.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                    .replace("{{writer}}",p.getWriter().getName()));
        }
        html = html.replace("<li id=\"replace\"></li>", sb.toString());

        if (loggedInUser == null) {
            return html.getBytes();
        } else {
            return html.replace("<li><a href=\"user/login.html\" role=\"button\">로그인</a></li>", HtmlTemplate.USER_ID.replace("{}", loggedInUser.getUserId()))
                    .replace("<li><a href=\"user/form.html\" role=\"button\">회원가입</a></li>", HtmlTemplate.LOGOUT)
                    .getBytes();
        }
    }
}
