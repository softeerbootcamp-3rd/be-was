package db;

import com.google.common.collect.Maps;

import model.Post;
import model.User;
import org.h2.tools.Server;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Database {
    private static final String URL = "jdbc:h2:./data/test"; // H2 데이터베이스 URL
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    static {
        // H2 데이터베이스 연결 및 테이블 생성
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);) {

            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS users (userId VARCHAR(255) PRIMARY KEY, password VARCHAR(255), name VARCHAR(255), email VARCHAR(255))");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS posts (id INT AUTO_INCREMENT PRIMARY KEY, writer VARCHAR(255), title VARCHAR(255), contents VARCHAR(1000), fileId VARCHAR(1000), fileExtension VARCHAR(1000), createdAt TIMESTAMP, commentCount INT)");
            statement.close();
            connection.close();

            // H2 데이터베이스 콘솔을 시작합니다.
            Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Database() {
        throw new IllegalStateException("Database class");
    }

    public static void addUser(User user) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users (userId, password, name, email) VALUES (?, ?, ?, ?)");) {

            preparedStatement.setString(1, user.getUserId());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getName());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static User findUserById(String userId) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE userId = ?");) {

            preparedStatement.setString(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                User user = new User(
                        resultSet.getString("userId"),
                        resultSet.getString("password"),
                        resultSet.getString("name"),
                        resultSet.getString("email")
                );
                resultSet.close();
                preparedStatement.close();
                connection.close();
                return user;
            }

            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Collection<User> findAll() {
        List<User> userList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD); Statement statement = connection.createStatement();) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users");

            while (resultSet.next()) {
                User user = new User(
                        resultSet.getString("userId"),
                        resultSet.getString("password"),
                        resultSet.getString("name"),
                        resultSet.getString("email")
                );
                userList.add(user);
            }

            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }

    public static void addPost(Post post) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO posts (writer, title, contents, fileId, fileExtension, createdAt, commentCount) VALUES (?, ?, ?, ?, ?, ?, ?)");) {

            preparedStatement.setString(1, post.getWriter());
            preparedStatement.setString(2, post.getTitle());
            preparedStatement.setString(3, post.getContents());
            preparedStatement.setString(4, post.getFileId());
            preparedStatement.setString(5, post.getFileExtension());

            // 현재 시간을 가져와서 연월일 시분까지의 문자열로 변환하여 저장
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String formattedDateTime = now.format(formatter);
            preparedStatement.setString(6, formattedDateTime);

            preparedStatement.setInt(7, 0);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Post getPostById(int postId) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM posts WHERE id = ?");) {

            preparedStatement.setInt(1, postId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Post post = new Post(
                        resultSet.getInt("id"),
                        resultSet.getString("writer"),
                        resultSet.getString("title"),
                        resultSet.getString("contents"),
                        resultSet.getString("fileId"),
                        resultSet.getString("fileExtension"),
                        resultSet.getTimestamp("createdAt")
                );
                resultSet.close();
                preparedStatement.close();
                connection.close();
                return post;
            }

            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Collection<Post> getAllPosts() {
        List<Post> postList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement();) {

            ResultSet resultSet = statement.executeQuery("SELECT * FROM posts");

            while (resultSet.next()) {
                Post post = new Post(
                        resultSet.getInt("id"),
                        resultSet.getString("writer"),
                        resultSet.getString("title"),
                        resultSet.getString("contents"),
                        resultSet.getString("fileId"),
                        resultSet.getString("fileExtension"),
                        resultSet.getTimestamp("createdAt")
                );
                postList.add(post);
            }

            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return postList;
    }
}
