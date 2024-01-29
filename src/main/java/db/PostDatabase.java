package db;

import com.google.common.collect.Maps;
import model.Post;

import java.util.*;

public class PostDatabase {
    private static Long lastId = 0L;
    private static final Map<Long, Post> posts = Maps.newHashMap();

    public static void add(Post post) {
        post.setId(lastId + 1);
        lastId += 1;

        posts.put(post.getId(), post);
    }

    public static Post findById(Long id) {
        return posts.get(id);
    }

    public static void deleteById(Long id) {
        posts.remove(id);
    }

    public static Collection<Post> findAll() {
        return posts.values();
    }

    public static int countAll() {
        return posts.size();
    }

    public static Collection<Post> getPage(int pageSize, int pageNumber) {
        List<Post> allPosts = new ArrayList<>(posts.values());
        Collections.reverse(allPosts);

        int totalPosts = allPosts.size();
        int startIndex = (pageNumber - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalPosts);

        return allPosts.subList(startIndex, endIndex);
    }
}
