package util.template;

import model.Post;
import model.User;
import util.ResourceUtils;
import util.StringUtils;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Map;

public class IndexTemplate {
    public static final String USERNAME = "<li><a role=\"button\">{}</a></li>";
    public static final String LOGOUT = "<li><a href=\"/user/logout\" role=\"button\">로그아웃</a></li>";
    public static final String POST = "           <li>\n" +
            "                  <div class=\"wrap\">\n" +
            "                      <div class=\"main\">\n" +
            "                          <strong class=\"subject\">\n" +
            "                              <a href=\"/board/show/{postId}\">{title}</a>\n" +
            "                          </strong>\n" +
            "                          <div class=\"auth-info\">\n" +
            "                              <i class=\"icon-add-comment\"></i>\n" +
            "                              <span class=\"time\">{created}</span>\n" +
            "                              <a href=\"./user/profile\" class=\"author\">{writer}</a>\n" +
            "                          </div>\n" +
            "                          <div class=\"reply\" title=\"댓글\">\n" +
            "                              <i class=\"icon-reply\"></i>\n" +
            "                              <span class=\"point\">{number_of_comment}</span>\n" +
            "                          </div>\n" +
            "                      </div>\n" +
            "                  </div>\n" +
            "              </li>";

    public static byte[] render(User loggedInUser, Collection<Post> posts, Map<String, String> queryMap) throws IOException {
        StringBuilder sb = new StringBuilder();
        String srchTerm = queryMap.get("srch-term");
        if (StringUtils.hasLength(srchTerm)) {
            srchTerm = StringUtils.decode(srchTerm);
        }

        String html = new String(ResourceUtils.getStaticResource("/index.html"));
        for (Post p : posts) {
            if (StringUtils.hasLength(srchTerm) && !p.getTitle().contains(srchTerm))
                continue;
            sb.append(POST.replace("{postId}", p.getPostId().toString())
                    .replace("{title}", p.getTitle())
                    .replace("{created}", p.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                    .replace("{writer}",p.getWriter().getName())
                    .replace("{number_of_comment}", String.valueOf(p.getComments().size()))
            );
        }
        html = html.replace("<li id=\"replace\"></li>", sb.toString());

        if (loggedInUser == null) {
            return html.getBytes();
        } else {
            return html.replace("<li><a href=\"user/login.html\" role=\"button\">로그인</a></li>", USERNAME.replace("{}", loggedInUser.getName()))
                    .replace("<li><a href=\"user/form.html\" role=\"button\">회원가입</a></li>", LOGOUT)
                    .getBytes();
        }
    }
}
