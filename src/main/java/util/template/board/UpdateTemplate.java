package util.template.board;

import model.Post;
import model.User;
import util.ResourceUtils;
import java.io.IOException;

public class UpdateTemplate {
    public static final String USERNAME = "<li><a role=\"button\">{}</a></li>";
    private static final String FORM = "<form name=\"question\" method=\"post\" action=\"/board/update/{postId}\">\n" +
            "                <div class=\"form-group\">\n" +
            "                    <label for=\"title\">제목</label>\n" +
            "                    <input type=\"text\" class=\"form-control\" id=\"title\" name=\"title\" value=\"{title}\" required/>\n" +
            "                </div>\n" +
            "                <div class=\"form-group\">\n" +
            "                    <label for=\"contents\">내용</label>\n" +
            "                    <textarea name=\"contents\" id=\"contents\" rows=\"5\" class=\"form-control\" required>{contents}</textarea>\n" +
            "                </div>\n" +
            "                <button type=\"submit\" class=\"btn btn-success clearfix pull-right\">수정하기</button>\n" +
            "                <div class=\"clearfix\" />\n" +
            "            </form>";

    public static byte[] render(User loggedInUser, Post post) throws IOException {
        String username = USERNAME.replace("{}", loggedInUser.getName());

        String form = FORM.replace("{postId}", post.getPostId().toString())
                .replace("{title}", post.getTitle())
                .replace("{contents}", post.getContents());

        String html = new String(ResourceUtils.getStaticResource("/board/update.html"));

        return html.replace("<li id=\"username\"></li>", username)
                .replace("<div id=\"replace\"></div>", form)
                .getBytes();
    }
}
