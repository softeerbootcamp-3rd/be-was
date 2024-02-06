package database;

import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

public class UserRepository {
    public static void add(User user) {
        String query = "INSERT INTO users (userId, password, name, email) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = H2Database.getConnection().prepareStatement(query)) {
            statement.setString(1, user.getUserId());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getName());
            statement.setString(4, user.getEmail());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static User findByUserId(String userId) {
        String query = "SELECT * FROM users WHERE userId = ?";
        try (PreparedStatement statement = H2Database.getConnection().prepareStatement(query)) {
            statement.setString(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return User.of(resultSet);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static User findByIdOrEmpty(String userId) {
        User user = findByUserId(userId);
        if (user == null)
            return new User("", "", "(알 수 없음)", "");
        return user;
    }

    public static Collection<User> findAll() {
        String query = "SELECT * FROM users";
        try (Statement statement = H2Database.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            Collection<User> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(User.of(resultSet));
            }
            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
