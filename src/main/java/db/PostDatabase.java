package db;

import model.Post;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PostDatabase {
    private static final Map<String, Post> posts = new ConcurrentHashMap<>();

    public static String addPost(Post post) {
        posts.put(post.getPostId(), post);
        return post.getPostId();
    }

    public static Post findPostById(String postId) {
        if (postId == null) return null;
        return posts.get(postId);
    }

    public static Collection<Post> findAll() {
        return posts.values();
    }
}
