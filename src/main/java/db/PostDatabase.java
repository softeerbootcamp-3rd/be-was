package db;

import com.google.common.collect.Maps;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import model.Post;

public class PostDatabase {

    private static final Connection connection;
    private static long id = 0L;

    static {
        String url = "jdbc:h2:tcp://localhost/~/was";
        String username = "sa";
        String password = "";

        try {
            connection = DriverManager.getConnection(url, username, password);

            String sql = "CREATE TABLE IF NOT EXISTS Posts (\n"
                    + "    postId BIGINT PRIMARY KEY,\n"
                    + "    writer VARCHAR(255),\n"
                    + "    title VARCHAR(255),\n"
                    + "    contents TEXT,\n"
                    + "    postTime TIMESTAMP,\n"
                    + "    reply INT,\n"
                    + "    imageName VARCHAR(255)\n"
                    + ");";

            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);

            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static final Map<Long, Post> posts = Maps.newHashMap();

    public static Post findPostById(long postId) {

        String sql = "SELECT * FROM Posts WHERE postId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, postId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                long findPostId = resultSet.getLong("postId");
                String writer = resultSet.getString("writer");
                String title = resultSet.getString("title");
                String contents = resultSet.getString("contents");
                LocalDateTime postTime = resultSet.getObject("postTime", LocalDateTime.class);
                int reply = resultSet.getInt("reply");
                String imageName = resultSet.getString("imageName");

                return new Post(findPostId, writer, title, contents, postTime, imageName);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException();
        }
    }

    public static Collection<Post> findAll() {

        Map<Long, Post> posts = new HashMap<>();
        String sql = "SELECT * FROM Posts";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long postId = resultSet.getLong("postId");
                String writer = resultSet.getString("writer");
                String title = resultSet.getString("title");
                String contents = resultSet.getString("contents");
                LocalDateTime postTime = resultSet.getObject("postTime", LocalDateTime.class);
                int reply = resultSet.getInt("reply");
                String imageName = resultSet.getString("imageName");

                posts.put(postId, new Post(postId, writer, title, contents, postTime, imageName));
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException();
        }

        return posts.values();
    }

    public static void addPost(String writer, String title, String contents, LocalDateTime time, String imageName) {

        String sql = "INSERT INTO Posts (postId, writer, title, contents, postTime, reply, imageName) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, ++id);
            preparedStatement.setString(2, writer);
            preparedStatement.setString(3, title);
            preparedStatement.setString(4, contents);
            preparedStatement.setObject(5, time);
            preparedStatement.setInt(6, 0);
            preparedStatement.setString(7, imageName);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException();
        }
    }
}
