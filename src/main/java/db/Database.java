package db;

import com.google.common.collect.Maps;

import model.Post;
import model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Database {
    private static Map<String, User> users = Maps.newHashMap();
    private static Map<Long, Post> postList = Maps.newHashMap();

    public static void addUser(User user) {
        users.put(user.getUserId(), user)   ;
    }
    public static void addPost(Post post) {
        postList.put(post.getId(), post);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static Collection<User> findAll() {
        return users.values();
    }

    // 테스트 용도
    public static void clear() {
        users.clear();
    }

    public static Map<Long, Post> getPostList() {
        return postList;
    }

    public static Post getPostById(Long id) {
        return postList.get(id);
    }
}
