package database;

import model.Post;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

public class PostRepository {

    public static Long add(Post post) {
        String query = "INSERT INTO posts (writerId, title, contents, createDatetime) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = H2Database.getConnection().prepareStatement(query)) {
            statement.setString(1, post.getWriterId());
            statement.setString(2, post.getTitle());
            statement.setString(3, post.getContents());
            statement.setTimestamp(4, new Timestamp(post.getCreateDatetime().getTime()));
            statement.executeUpdate();

            // 생성된 ID 값 얻기
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                } else {
                    throw new SQLException("Failed to get generated key, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Post findById(Long id) {
        String query = "SELECT * FROM posts WHERE id = ?";
        try (PreparedStatement statement = H2Database.getConnection().prepareStatement(query)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Post.of(resultSet);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteById(Long id) {
        String query = "DELETE FROM posts WHERE id = ?";
        try (PreparedStatement statement = H2Database.getConnection().prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static int countAll() {
        String query = "SELECT COUNT(*) FROM users";
        try (PreparedStatement statement = H2Database.getConnection().prepareStatement(query)) {
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

    public static Collection<Post> getPage(int pageSize, int pageNumber) {
        String query = "SELECT * FROM posts ORDER BY id DESC LIMIT ? OFFSET ?";
        List<Post> posts = new ArrayList<>();

        try (PreparedStatement statement = H2Database.getConnection().prepareStatement(query)) {
            int offset = (pageNumber - 1) * pageSize;

            statement.setInt(1, pageSize);
            statement.setInt(2, offset);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    posts.add(Post.of(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return posts;
    }
}
