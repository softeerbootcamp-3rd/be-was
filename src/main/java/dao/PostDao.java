package dao;

import dto.PostDto;
import model.Post;
import model.User;
import util.JdbcUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PostDao {

    private static final Connection connection = JdbcUtil.getJdbcConnection();
    private static long postId = 10;

    public static long insertPost(Post post) {
        String query = "INSERT INTO posts (post_id, title, content, author_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setLong(1, postId);
            preparedStatement.setString(2, post.getTitle());
            preparedStatement.setString(3, post.getContent());
            preparedStatement.setString(4, post.getAuthorId());

            preparedStatement.executeUpdate();

            return postId++;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static Post findPostByPostId(long postId) {
        String query = "SELECT * FROM posts WHERE POST_ID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, postId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Post post = new Post(
                            resultSet.getString("title"),
                            resultSet.getString("content"),
                            resultSet.getString("author_id")
                    );
                    return post;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<PostDto> findAll() {
        String query = "SELECT * FROM posts";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            List<PostDto> postList = new ArrayList<>();

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    PostDto post = new PostDto(
                            resultSet.getLong("post_id"),
                            resultSet.getString("title"),
                            resultSet.getString("content"),
                            resultSet.getString("author_id"),
                            resultSet.getString("created_at")
                    );
                    postList.add(post);
                }
            }

            return postList;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


}
