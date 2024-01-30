package db;

import com.google.common.collect.Maps;
import model.Comment;
import model.Post;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class PostRepository {

    private static PostRepository instance = new PostRepository();

    private static Map<Long, Post> posts = Maps.newHashMap();

    private static Long postCnt = 0L;

    public static PostRepository getInstance() { return instance; }

    public static void addPost(Post post) {
        ++postCnt;
        posts.put(postCnt, new Post(postCnt, post));
    }

    public static Post findByPostId(Long postId) { return posts.get(postId); }

    public static Collection<Post> findAll() { return posts.values(); }

    public static void addComment(Long postId, Comment comment) {
        Post post = posts.get(postId);
        List<Comment> commentList = post.getCommentList();
        comment.setCommentId(commentList.size()+1L);
        commentList.add(comment);
    }

}
