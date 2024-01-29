package util.template;

import model.Comment;
import model.Post;
import model.User;
import util.ResourceUtils;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;

public class ShowTemplate {
    public static final String USERNAME = "<li><a role=\"button\">{}</a></li>";

    public static final String COMMENT = "                          <article class=\"article\" id=\"answer-1405\">\n" +
            "                              <div class=\"article-header\">\n" +
            "                                  <div class=\"article-header-thumb\">\n" +
            "                                      <img src=\"https://graph.facebook.com/v2.3/1324855987/picture\" class=\"article-author-thumb\" alt=\"\">\n" +
            "                                  </div>\n" +
            "                                  <div class=\"article-header-text\">\n" +
            "                                      <a href=\"/users/1/자바지기\" class=\"article-author-name\">{writer}</a>\n" +
            "                                      <a href=\"#answer-1434\" class=\"article-header-time\" title=\"퍼머링크\">{created}</a>\n" +
            "                                  </div>\n" +
            "                              </div>\n" +
            "                              <div class=\"article-doc comment-doc\">\n" +
            "                                  <p>{body}</p>\n" +
            "                              </div>\n" +
            "                              <div class=\"article-utils\">\n" +
            "                                  <ul class=\"article-utils-list\">\n" +
            "                                      <li>\n" +
            "                                          <form class=\"delete-answer-form\" action=\"/questions/413/answers/1405\" method=\"POST\">\n" +
            "                                              <input type=\"hidden\" name=\"_method\" value=\"DELETE\">\n" +
            "                                              <button type=\"submit\" class=\"delete-answer-button\">삭제</button>\n" +
            "                                          </form>\n" +
            "                                      </li>\n" +
            "                                  </ul>\n" +
            "                              </div>\n" +
            "                          </article>\n";
    public static final String TEMPLATE = "<div class=\"container\" id=\"main\">\n" +
            "    <div class=\"col-md-12 col-sm-12 col-lg-12\">\n" +
            "        <div class=\"panel panel-default\">\n" +
            "          <header class=\"qna-header\">\n" +
            "              <h2 class=\"qna-title\">{title}</h2>\n" +
            "          </header>\n" +
            "          <div class=\"content-main\">\n" +
            "              <article class=\"article\">\n" +
            "                  <div class=\"article-header\">\n" +
            "                      <div class=\"article-header-thumb\">\n" +
            "                          <img src=\"https://graph.facebook.com/v2.3/100000059371774/picture\" class=\"article-author-thumb\" alt=\"\">\n" +
            "                      </div>\n" +
            "                      <div class=\"article-header-text\">\n" +
            "                          <a href=\"/users/92/kimmunsu\" class=\"article-author-name\">{author}</a>\n" +
            "                          <a href=\"/questions/413\" class=\"article-header-time\" title=\"퍼머링크\">\n" +
            "                              {created}\n" +
            "                              <i class=\"icon-link\"></i>\n" +
            "                          </a>\n" +
            "                      </div>\n" +
            "                  </div>\n" +
            "                  <div class=\"article-doc\">\n" +
            "                      <p>{contents}</p>\n" +
            "                  </div>\n" +
            "                  <div class=\"article-utils\">\n" +
            "                      <ul class=\"article-utils-list\">\n" +
            "                          <li>\n" +
            "                              <a class=\"link-modify-article\" href=\"/questions/423/form\">수정</a>\n" +
            "                          </li>\n" +
            "                          <li>\n" +
            "                              <form class=\"form-delete\" action=\"/questions/423\" method=\"POST\">\n" +
            "                                  <input type=\"hidden\" name=\"_method\" value=\"DELETE\">\n" +
            "                                  <button class=\"link-delete-article\" type=\"submit\">삭제</button>\n" +
            "                              </form>\n" +
            "                          </li>\n" +
            "                      </ul>\n" +
            "                  </div>\n" +
            "              </article>\n" +
            "\n" +
            "              <div class=\"qna-comment\">\n" +
            "                  <div class=\"qna-comment-slipp\">\n" +
            "                      <p class=\"qna-comment-count\"><strong>{number_of_comment}</strong>개의 댓글</p>\n" +
            "                      <div class=\"qna-comment-slipp-articles\">\n" +
            "\n" +
            "{comments}" +
            "                          <form class=\"answer-form\" method=\"post\" action=\"/board/comment\">\n" +
            "                              <div class=\"form-group\">\n" +
            "                                  <input type=\"hidden\" name=\"postId\" value=\"{postId}\">\n" +
            "                              </div>\n" +
            "                              <div class=\"form-group\" style=\"padding:14px;\">\n" +
            "                                  <textarea class=\"form-control\" name=\"body\" id=\"body\" required></textarea>\n" +
            "                              </div>\n" +
            "                              <button class=\"btn btn-success pull-right\" type=\"submit\">댓글달기</button>\n" +
            "                              <div class=\"clearfix\" />\n" +
            "                          </form>\n" +
            "                      </div>\n" +
            "                  </div>\n" +
            "              </div>\n" +
            "          </div>\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</div>";



    public static byte[] render(User loggedInUser, Post post) throws IOException {
        StringBuilder sb = new StringBuilder();

        List<Comment> comments = post.getComments();
        for (Comment comment : comments) {
            sb.append(COMMENT.replace("{writer}", comment.getWriter().getName())
                            .replace("{created}", comment.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                            .replace("{body}", comment.getBody()));
        }

        String template = TEMPLATE.replace("{author}", post.getWriter().getName())
                .replace("{created}", post.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .replace("{title}", post.getTitle())
                .replace("{contents}", post.getContents())
                .replace("{number_of_comment}", String.valueOf(post.getComments().size()))
                .replace("{comments}", sb.toString())
                .replace("{postId}", post.getPostId().toString());

        String html = new String(ResourceUtils.getStaticResource("/board/show.html"));

        return html.replace("<div id=\"replace\"></div>", template)
                .replace("<li id=\"username\"></li>", USERNAME.replace("{}", loggedInUser.getName()))
                .getBytes();
    }
}
