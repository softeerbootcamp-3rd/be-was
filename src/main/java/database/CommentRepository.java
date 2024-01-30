package database;

import entity.Comment;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CommentRepository {
    public static void add(Comment comment) {
        String query = "INSERT INTO comments (boardId, writerId, contents, createDatetime) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = H2Database.getConnection().prepareStatement(query)) {
            statement.setLong(1, comment.getBoardId());
            statement.setString(2, comment.getWriterId());
            statement.setString(3, comment.getContents());
            statement.setTimestamp(4, new Timestamp(comment.getCreateDatetime().getTime()));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Comment findById(Long id) {
        String query = "SELECT * FROM comments WHERE id = ?";
        try (PreparedStatement statement = H2Database.getConnection().prepareStatement(query)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Comment.of(resultSet);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Collection<Comment> findByBoardId(Long boardId) {
        String query = "SELECT * FROM comments WHERE boardId = ?";
        List<Comment> comments = new ArrayList<>();

        try (PreparedStatement statement = H2Database.getConnection().prepareStatement(query)) {
            statement.setLong(1, boardId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    comments.add(Comment.of(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return comments;
    }

    public static int countByBoardId(Long boardId) {
        String query = "SELECT COUNT(*) FROM comments WHERE boardId = ?";
        try (PreparedStatement statement = H2Database.getConnection().prepareStatement(query)) {
            statement.setLong(1, boardId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public static void deleteById(Long id) {
        String query = "DELETE FROM comments WHERE id = ?";
        try (PreparedStatement statement = H2Database.getConnection().prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteByBoardId(Long boardId) {
        String query = "DELETE FROM comments WHERE boardId = ?";
        try (PreparedStatement statement = H2Database.getConnection().prepareStatement(query)) {
            statement.setLong(1, boardId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
