package db;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Map;
import model.Post;

public class PostDatabase {

    private static final Map<Long, Post> posts = Maps.newHashMap();

    private static long id = 0L;

    public static void addPost(Post post) throws IllegalArgumentException {
        posts.put(++id, post);
    }

    public static Post findPostById(long postId) {
        return posts.get(postId);
    }

    public static Collection<Post> findAll() {
        return posts.values();
    }

    public static void clear() {
        posts.clear();
    }
}
