package util.html;

import db.CommentDatabase;
import db.PostDatabase;
import db.UserDatabase;
import exception.ResourceNotFoundException;
import model.Comment;
import model.Post;
import model.User;
import util.web.SharedData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class PostHtml {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static final int pageSize = 10;
    private static final int paginationSize = 5;

    public static String postList(String template) {
        int pageNumber = SharedData.getParamDataOrElse("page", Integer.class, 1);
        List<Post> postList = new ArrayList<>(PostDatabase.getPage(pageSize, pageNumber));

        StringBuilder sb = new StringBuilder();
        for (Post post : postList) {
            User writer = UserDatabase.findByIdOrEmpty(post.getWriterId());
            sb.append(template.replace("<!--post-id-->", Long.toString(post.getId()))
                    .replace("<!--title-->", post.getTitle())
                    .replace("<!--create-date-->", dateFormat.format(post.getCreateDatetime()))
                    .replace("<!--user-name-->", writer.getName())
                    .replace("<!--comments-->", Long.toString(CommentDatabase.countByPostId(post.getId()))));
        }
        return sb.toString();
    }

    public static String pagination(String template) {
        int pageNumber = SharedData.getParamDataOrElse("page", Integer.class, 1);
        int startPage = ((pageNumber - 1) / paginationSize) * paginationSize + 1;
        int endPage = startPage + paginationSize - 1;

        int totalPages = (int) Math.ceil((double) PostDatabase.countAll() / pageSize);
        if (totalPages == 0) totalPages = 1;

        StringBuilder sb = new StringBuilder();
        if (startPage > 1)
            sb.append(template.replace("<!--link-->", Integer.toString(startPage - 1))
                    .replace("<!--page-number-->", "«"));
        for (int i = startPage; i <= Math.min(totalPages, endPage); i++) {
            String temp = template.replace("<!--link-->", Integer.toString(i))
                    .replace("<!--page-number-->", Integer.toString(i));
            if (pageNumber == i)
                temp = temp.replace("<!--active-->", "active");
            sb.append(temp);
        }
        if (endPage < totalPages)
            sb.append(template.replace("<!--link-->", Integer.toString(endPage + 1))
                    .replace("<!--page-number-->", "»"));
        return sb.toString();
    }

    public static String postBtn(String template) {
        if (SharedData.requestUser.get() == null)
            return template.replace("<!--link-->", "/user/login.html");
        return template.replace("<!--link-->", "/post/write.html");
    }

    public static String postContent(String template) {
        Long postId = SharedData.getParamDataNotEmpty("postId", Long.class);
        Post post = PostDatabase.findById(postId);
        if (post == null)
            throw new ResourceNotFoundException(SharedData.request.get().getPath());
        return template.replace("<!--title-->", post.getTitle())
                .replace("<!--writer-->", UserDatabase.findByIdOrEmpty(post.getWriterId()).getName())
                .replace("<!--create-date-->", dateFormat.format(post.getCreateDatetime()))
                .replace("<!--contents-->", "<p>" + post.getContents().replace("\n", "</br>") + "</p>")
                .replace("<!--post-id-->", Long.toString(post.getId()))
                .replace("<!--comment-count-->", Long.toString(CommentDatabase.countByPostId(postId)));
    }

    public static String postBtnGroup(String template) {
        Long postId = SharedData.getParamDataNotEmpty("postId", Long.class);
        Post post = PostDatabase.findById(postId);
        User user = SharedData.requestUser.get();
        if (!Objects.equals(user.getUserId(), post.getWriterId()))
            return "";
        return template.replace("<!--post-id-->", Long.toString(postId));
    }

    public static String comments(String template) {
        Long postId = SharedData.getParamDataNotEmpty("postId", Long.class);
        Collection<Comment> comments = CommentDatabase.findByPostId(postId);

        StringBuilder sb = new StringBuilder();
        for (Comment comment : comments) {
            User writer = UserDatabase.findByIdOrEmpty(comment.getWriterId());
            sb.append(template.replace("<!--writer-->", writer.getName())
                    .replace("<!--create-date-->", dateFormat.format(comment.getCreateDatetime()))
                    .replace("<!--content-->", comment.getContent().replace("\n", "</br>"))
                    .replace("<!--comment-btn-group-->", commentBtnGroup(postId, comment)));
        }
        return sb.toString();
    }

    private static String commentBtnGroup(Long postId, Comment comment) {
        User user = SharedData.requestUser.get();
        if (user == null || !Objects.equals(user.getUserId(), comment.getWriterId()))
            return "";
        return HtmlTemplates.get("<!--comment-btn-group-->").getTemplate()
                .replace("<!--post-id-->", Long.toString(postId))
                .replace("<!--comment-id-->", Long.toString(comment.getId()));
    }

    public static String commentForm(String template) {
        Long postId = SharedData.getParamDataNotEmpty("postId", Long.class);
        return template.replace("<!--post-id-->", Long.toString(postId));
    }
}
