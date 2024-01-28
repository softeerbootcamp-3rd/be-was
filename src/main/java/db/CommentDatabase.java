package db;

import com.google.common.collect.Maps;
import model.Comment;

import java.util.Collection;
import java.util.Map;
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

    public static Collection<Comment> findByQnaId(Long qnaId) {
        return comments.values().stream()
                .filter(comment -> comment.getQnaId().equals(qnaId))
                .collect(Collectors.toList());
    }

    public static Long countByQnaId(Long qnaId) {
        return comments.values().stream()
                .filter(comment -> comment.getQnaId().equals(qnaId))
                .count();
    }

    public static Collection<Comment> findAll() {
        return comments.values();
    }
}
