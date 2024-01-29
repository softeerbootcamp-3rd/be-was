package db;

import com.google.common.collect.Maps;
import model.Post;

import java.util.Collection;
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

}
