package db;

import model.Post;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static db.H2Database.*;

public class PostRepository {
    private static final Logger logger = LoggerFactory.getLogger(PostRepository.class);
    private static Connection conn = null;
    private static PreparedStatement preparedStatement = null;
    private static ResultSet resultSet = null;

    public static void addPost(Post post) {
        try {
            conn = DriverManager.getConnection(url, username, password);

            String sql = "INSERT INTO Post (writer, title, content) VALUES (?, ?, ?)";
            preparedStatement = conn.prepareStatement(sql);

            preparedStatement.setString(1, post.getWriter());
            preparedStatement.setString(2, post.getTitle());
            preparedStatement.setString(3, post.getContent());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        } finally {
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
        }
    }

    public static Collection<Post> findAll() {
        List<Post> posts = new ArrayList<>();
        try {
            conn = DriverManager.getConnection(url, username, password);
            String sql = "SELECT * FROM Post";
            preparedStatement = conn.prepareStatement(sql);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String userId = resultSet.getString("writer");
                String password = resultSet.getString("title");
                String name = resultSet.getString("content");
                posts.add(new Post(userId, password, name));
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
        }
        return posts;
    }
}
