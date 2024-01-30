package db;

import db.dto.CreatePost;
import db.dto.GetPost;
import model.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static db.H2Database.*;

public class PostRepository {
    private static final Logger logger = LoggerFactory.getLogger(PostRepository.class);
    private static Connection conn = null;
    private static PreparedStatement preparedStatement = null;
    private static ResultSet resultSet = null;

    public static void addPost(CreatePost post) {
        try {
            conn = DriverManager.getConnection(url, username, password);

            String sql = "INSERT INTO Post (writer, title, content,createdtime ,commentcount) VALUES (?, ?, ?, ?, ?)";
            preparedStatement = conn.prepareStatement(sql);

            LocalDateTime now = LocalDateTime.now();
            Timestamp timestamp = Timestamp.valueOf(now);

            preparedStatement.setString(1, post.getWriter());
            preparedStatement.setString(2, post.getTitle());
            preparedStatement.setString(3, post.getContent());
            preparedStatement.setTimestamp(4, timestamp);
            preparedStatement.setInt(5, 0);

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

    public static Collection<GetPost> findAll() {
        List<GetPost> posts = new ArrayList<>();
        try {
            conn = DriverManager.getConnection(url, username, password);
            String sql = "SELECT * FROM Post";
            preparedStatement = conn.prepareStatement(sql);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String userId = resultSet.getString("writer");
                String password = resultSet.getString("title");
                String name = resultSet.getString("content");
                Timestamp timestamp = resultSet.getTimestamp("createdtime");
                int commentCount = resultSet.getInt("commentcount");

                posts.add(new GetPost(id, userId, password, name, timestamp.toLocalDateTime(),commentCount ));
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
