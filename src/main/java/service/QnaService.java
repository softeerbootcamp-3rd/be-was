package service;

import db.PostDatabase;
import model.Post;
import model.User;
import util.HtmlBuilder;

import java.util.Map;

public class QnaService {
    public String writePost(Map<String, String> writePostParams, User user) {
        String title = writePostParams.get("title");
        String contents = writePostParams.get("contents");

        if (title != null && contents != null) {
            return PostDatabase.addPost(new Post(user, title, contents));
        }
        throw new IllegalArgumentException("Invalid Parameters");
    }

    public byte[] printWriteDetailPage(Map<String, String> queryString, User user) {
        String postId = queryString.get("postId");
        Post post = PostDatabase.findPostById(postId);
        if (post == null) {
            throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
        }
        return HtmlBuilder.buildWriteDetailPage(post, user);
    }
}
