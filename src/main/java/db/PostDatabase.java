package db;

import com.google.common.collect.Maps;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import model.Post;

public class PostDatabase {

    private static final Map<Long, Post> posts = Maps.newHashMap();

    private static long id = 0L;

    public static Post findPostById(long postId) {
        return posts.get(postId);
    }

    public static Collection<Post> findAll() {
        return posts.values();
    }

    public static void clear() {
        posts.clear();
    }

    public static void addPost(String writer, String title, String contents, LocalDateTime time, String imagePath) {
        Post post = new Post(++id, writer, title, contents, time, imagePath);
        posts.put(post.getPostId(), post);
    }
}
