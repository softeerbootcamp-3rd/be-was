package database;

import entity.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class UserRepository {
    public static void add(User user) {
        String query = "insert: users: userId, password, name, email: ?, ?, ?, ?";
        try (PreparedStatement statement = CsvDatabase.getConnection().prepareStatement(query)) {
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
        String query = "select: users: userId: ?";
        try (PreparedStatement statement = CsvDatabase.getConnection().prepareStatement(query)) {
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
        String query = "select: users";
        try (PreparedStatement statement = CsvDatabase.getConnection().prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
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
