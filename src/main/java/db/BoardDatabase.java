package db;

import model.Post;
import java.util.ArrayList;
import java.util.List;

public class BoardDatabase {
    private static List<Post> posts = new ArrayList<>();

    public static void addPost(Post post) {
        posts.add(post);
    }

    public static List<Post> findAll() {
        return posts;
    }
}
