package dao;

import dto.PostDto;
import model.Post;
import model.User;
import util.JdbcUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostDao {

    private static final Connection connection = JdbcUtil.getJdbcConnection();

    public static int insertPost(Post post) {
        String query = "INSERT INTO posts (title, content, author_id) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, post.getTitle());
            preparedStatement.setString(2, post.getContent());
            preparedStatement.setString(3, post.getAuthorId());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                // 생성된 키 가져오기
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int postId = generatedKeys.getInt("post_id");
                        return postId;
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static List<PostDto> findAll() {
        String query = "SELECT * FROM posts LEFT JOIN users ON posts.author_id = users.user_id";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            List<PostDto> postList = new ArrayList<>();

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    PostDto post = new PostDto(
                            resultSet.getInt("post_id"),
                            resultSet.getString("title"),
                            resultSet.getString("content"),
                            resultSet.getString("userName"),
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


    public static PostDto findPostByPostId(int postId) {
        String query = "SELECT * FROM posts LEFT JOIN users ON posts.author_id = users.user_id WHERE post_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, postId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    PostDto post = new PostDto(
                            resultSet.getInt("post_id"),
                            resultSet.getString("title"),
                            resultSet.getString("content"),
                            resultSet.getString("userName"),
                            resultSet.getString("created_at")
                    );
                    return post;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


}
