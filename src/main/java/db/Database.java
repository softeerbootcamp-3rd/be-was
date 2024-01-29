package db;

import com.google.common.collect.Maps;

import model.Post;
import model.User;

import java.util.Collection;
import java.util.Map;

public class Database {
    private static Map<String, User> users = Maps.newHashMap();
    private static Map<String, Post> posts = Maps.newHashMap();

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static Collection<User> findAllUsers() {
        return users.values();
    }

    public static void addPost(Post post) {
        posts.put(post.getPostId(), post);
    }

    public static Post findPostById(String postId) {
        return posts.get(postId);
    }

    public static Collection<Post> findAllPosts() {
        return posts.values();
    }
}
