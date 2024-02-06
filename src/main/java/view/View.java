package view;

import db.Database;
import model.Post;
import model.User;

import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Map;

public enum View {
    CONTENT_LIST {
        @Override
        public String generate(Object object) {
            Map<Long, Post> postList = (Map<Long, Post>) object;
            StringBuilder sb = new StringBuilder();
            sb.append("<ul class=\"list\">");
            for (Post post : postList.values()) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                String createTime = post.getCreateTime().format(formatter);

                sb.append("<li>");
                sb.append("<div class=\"wrap\">");
                sb.append("<div class=\"main\">");
                sb.append("<strong class=\"subject\">");
                sb.append("<a href=\"./post/detail?id=" + post.getId() + "\">" + post.getTitle() + "</a>");
                sb.append("</strong>");
                sb.append("<div class=\"auth-info\">");
                sb.append("<i class=\"icon-add-comment\"></i>");
                sb.append("<span class=\"time\">"+createTime+"</span>");
                sb.append("<a href=\"./user/profile.html\" class=\"author\">"+ post.getWriter() +"</a>");
                sb.append("</div>");
                sb.append("<div class=\"reply\" title=\"댓글\">");
                sb.append("<i class=\"icon-reply\"></i>");
                sb.append("<span class=\"point\">0</span>");
                sb.append("</div>");
                sb.append("</div>");
                sb.append("</div>");
                sb.append("</li>");
            }
            sb.append("</ul>");
            return sb.toString();
        }
    },

    USER_LIST {
        @Override
        public String generate(Object object) {
            Collection<User> users = (Collection<User>) object;
            StringBuilder sb = new StringBuilder();
            int sequence = 0;
            sb.append("<tbody>");
            for (User user : users) {
                sb.append("<tr>");
                sb.append("<th scope=\"row\">");
                sb.append(++sequence);
                sb.append("</th> <td>");
                sb.append(user.getUserId());
                sb.append("</th> <td>");
                sb.append(user.getName());
                sb.append("</th> <td>");
                sb.append(user.getEmail());
                sb.append("</td><td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>");
                sb.append("</tr>");
            }
            sb.append("</tbody>");
            return sb.toString();
        }
    },

    POST_DETAIL {
        @Override
        public String generate(Object object) {
            String id = (String) object;
            Long postId = Long.parseLong(id);
            Post post = Database.getPostById(postId);

            StringBuilder sb = new StringBuilder();
            sb.append("<div class=\"panel panel-default\">");
            sb.append("<header class=\"qna-header\">\n");
            sb.append("<h2 class=\"qna-title\">" + post.getTitle() + "</h2>\n");
            sb.append("</header>\n");
            sb.append("<div class=\"content-main\">\n");
            sb.append("<article class=\"article\">\n");
            sb.append("<div class=\"article-header\">\n");
            sb.append("<div class=\"article-header-thumb\">\n");
            sb.append("<img src=\"https://graph.facebook.com/v2.3/100000059371774/picture\" class=\"article-author-thumb\" alt=\"\">\n");
            sb.append("</div>\n");
            sb.append("<div class=\"article-header-text\">\n");
            sb.append("<a href=\"/users/92/kimmunsu\" class=\"article-author-name\">" + post.getWriter() + "</a>\n");
            sb.append("<a href=\"/questions/413\" class=\"article-header-time\" title=\"퍼머링크\">\n");
            sb.append(post.getCreateTime() + "\n");
            sb.append("<i class=\"icon-link\"></i>\n");
            sb.append(" </a>\n");
            sb.append("</div>\n");
            sb.append("</div>\n");
            sb.append("<div class=\"article-doc\">\n");
            sb.append("<p>"+post.getContents()+"</p>\n");
            sb.append("</div>\n");
            sb.append("</article>\n");
            sb.append("</div>\n");
            sb.append("<div class=\"article-utils\">");

            return sb.toString();
        }
    };

    public abstract String generate(Object object);
}
