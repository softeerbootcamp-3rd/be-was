package db;

import model.Post;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BoardDatabase {
    private static Map<Long, Post> posts = new ConcurrentHashMap<>();

    public static void addPost(Post post) {
        posts.put(post.getPostId(), post);
    }

    public static Post findPostById(Long postId) {
        return posts.get(postId);
    }

    public static Collection<Post> findAll() {
        return posts.values();
    }

    public static void removePost(Long postId) {
        posts.remove(postId);
    }
}
