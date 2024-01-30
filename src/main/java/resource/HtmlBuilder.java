package resource;

import db.Database;
import model.Post;
import model.User;

import java.util.Collection;
import java.util.Map;

public class HtmlBuilder {
    private static final String FILE_PATH = "/data/";
    public static String generateUserList() {
        Collection<User> userList = Database.findAllUsers();
        StringBuilder sb = new StringBuilder();
        int cnt = 0;
        for (User user : userList) {
            cnt++;
            sb.append("<tr>\n").append("<th scope=\"row\">").append(cnt).append("</th>")
                    .append("<td>").append(user.getUserId()).append("</td>")
                    .append("<td>").append(user.getName()).append("</td>")
                    .append("<td>").append(user.getEmail()).append("</td>")
                    .append("<td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>")
                    .append("</tr>");
        }

        return sb.toString();
    }

    public static String generatePostList() {
        Collection<Post> posts = Database.findAllPosts();
        StringBuilder sb = new StringBuilder();

        int cnt = 0;
        for(Post post : posts) {
            cnt++;
            sb.append("<li>\n")
                    .append("<div class=\"wrap\">\n")
                    .append("<div class=\"main\">\n")
                    .append("<strong class=\"subject\">\n")
                    .append("<a href=\"./qna/show.html?id=").append(post.getPostId()).append("\">").append(post.getTitle()).append("</a>\n")
                    .append("</strong>\n")
                    .append("<div class=\"auth-info\">\n")
                    .append("<i class=\"icon-add-comment\"></i>\n")
                    .append("<span class=\"time\">").append(post.getDate()).append("</span>\n")
                    .append("<a href=\"./user/profile.html\" class=\"author\">").append(post.getWriter()).append("</a>\n")
                    .append("</div>\n")
                    .append("<div class=\"reply\" title=\"댓글\">")
                    .append("<i class=\"icon-reply\"></i>")
                    .append("<span class=\"point\">").append(cnt).append("</span>\n")
                    .append("</div>\n")
                    .append("</div>\n")
                    .append("</div>\n")
                    .append("</li>\n");

        }

        return sb.toString();
    }

    public static String generatePostInfo(Map<String, String> params) {
        Post post = Database.findPostById(params.get("id"));
        StringBuilder sb = new StringBuilder();

        if (post != null) {
            sb.append("<header class=\"qna-header\">\n")
                    .append("<h2 class=\"qna-title\">").append(post.getTitle()).append("</h2>\n")
                    .append("</header>\n")
                    .append("<div class=\"content-main\">\n")
                    .append("<article class=\"article\">\n")
                    .append("<div class=\"article-header\">\n")
                    .append("<div class=\"article-header-thumb\">\n")
                    .append("<img src=\"https://graph.facebook.com/v2.3/100000059371774/picture\" class=\"article-author-thumb\" alt=\"\">\n")
                    .append("</div>\n")
                    .append("<div class=\"article-header-text\">\n")
                    .append("<a href=\"/users/92/kimmunsu\" class=\"article-author-name\">").append(post.getWriter()).append("</a>\n")
                    .append("<a href=\"/questions/413\" class=\"article-header-time\" title=\"퍼머링크\">\n")
                    .append(post.getDate())
                    .append("\n<i class=\"icon-link\"></i>\n")
                    .append("</a>\n")
                    .append("</div>\n")
                    .append("</div>\n")
                    .append("<div class=\"article-doc\">\n")
                    .append("<p>").append(post.getContents()).append("</p>\n")
                    .append("</div>\n");
        }

        return sb.toString();
    }

    public static String generateAttachedFileInfo(Map<String, String> params) {
        Post post = Database.findPostById(params.get("id"));
        StringBuilder sb = new StringBuilder();

        if (post == null) {
            return "";
        }

        String fileName = post.getAttachedFileName();
        if (fileName != null && !fileName.isEmpty()) {
            String filePath = FILE_PATH + fileName;
            sb.append("<li>\n")
                .append("<a href=\"").append(filePath).append("\" download=\"").append(fileName).append("\">")
                .append(fileName).append("</a>\n")
                .append("</li>\n");
        }

        return sb.toString();
    }

    public static String generateProfileInfo(User user) {
        StringBuilder sb = new StringBuilder();

        sb.append("<h4 class=\"media-heading\">").append(user.getName()).append("</h4>\n")
                .append("<p>\n")
                .append("<a href=\"#\" class=\"btn btn-xs btn-default\"><span class=\"glyphicon glyphicon-envelope\"></span>").append(user.getEmail()).append("</a>\n")
                .append("</p>\n");

        return sb.toString();
    }
}
