package db;

import com.google.common.collect.Maps;
import model.Comment;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class CommentDatabase {

    private static Long lastId = 0L;
    private static final Map<Long, Comment> comments = Maps.newHashMap();

    public static void add(Comment comment) {
        comment.setId(lastId);
        lastId += 1;

        comments.put(comment.getId(), comment);
    }

    public static Comment findById(Long id) {
        return comments.get(id);
    }

    public static Collection<Comment> findByPostId(Long postId) {
        return comments.values().stream()
                .filter(comment -> Objects.equals(comment.getPostId(), (postId)))
                .collect(Collectors.toList());
    }

    public static Long countByPostId(Long postId) {
        return comments.values().stream()
                .filter(comment -> Objects.equals(comment.getPostId(), (postId)))
                .count();
    }

    public static void deleteById(Long commentId) {
        comments.remove(commentId);
    }

    public static void deleteByPostId(Long postId) {
        comments.entrySet().removeIf(entry -> Objects.equals(entry.getValue().getPostId(), postId));
    }

    public static Collection<Comment> findAll() {
        return comments.values();
    }
}
