package db;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class H2Database {
    private static final Logger logger = LoggerFactory.getLogger(H2Database.class);
    private static final String url = "jdbc:h2:tcp://localhost:9092/~/was";
    private static final String username = "sa";
    private static final String password = "";

    private static Connection conn = null;
    private static PreparedStatement preparedStatement = null;
    private static ResultSet resultSet = null;

    H2Database() {
        createUserTable();
    }

    public static void createUserTable() {
        String sql = "CREATE TABLE IF NOT EXISTS User (" +
                "userId VARCHAR(255) PRIMARY KEY," +
                "password VARCHAR(255) NOT NULL," +
                "name VARCHAR(255) NOT NULL," +
                "email VARCHAR(255) NOT NULL" +
                ");";

        try (Connection conn = DriverManager.getConnection(url, username, password); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }
    public static void adduser(User user) {
        try {
            conn = DriverManager.getConnection(url, username, password);

            String sql = "INSERT INTO User (userId, password, name, email) VALUES (?, ?, ?, ?)";
            preparedStatement = conn.prepareStatement(sql);

            preparedStatement.setString(1, user.getUserId());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getName());
            preparedStatement.setString(4, user.getEmail());

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

    public static User findUserById(String userId) {
       try {
           conn = DriverManager.getConnection(url, username, password);
           String sql = "SELECT * FROM User WHERE userId = ?";
           preparedStatement = conn.prepareStatement(sql);
           preparedStatement.setString(1, userId);

           resultSet = preparedStatement.executeQuery();
           if (resultSet.next()) {
               return new User(
                       resultSet.getString("userId"),
                       resultSet.getString("password"),
                       resultSet.getString("name"),
                       resultSet.getString("email")
               );
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
       return null;
    }

    public static Collection<User> findAll() {
        List<User> users = new ArrayList<>();
        try {
            conn = DriverManager.getConnection(url, username, password);
            String sql = "SELECT * FROM User";
            preparedStatement = conn.prepareStatement(sql);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String userId = resultSet.getString("userId");
                String password = resultSet.getString("password");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                users.add(new User(userId, password, name, email));
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
        return users;
    }

    public static boolean isValidLogin(String id, String pw){
        User user = findUserById(id);
        return user != null && user.getPassword().equals(pw);
    }
}
