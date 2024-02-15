package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import model.User;

import java.util.Collection;
import java.util.Map;

public class Database {

    private static final Connection connection;

    static {
        String url = "jdbc:h2:tcp://localhost/~/was";
        String username = "sa";
        String password = "";

        try {
            connection = DriverManager.getConnection(url, username, password);

            String sql = "CREATE TABLE IF NOT EXISTS Users (\n"
                    + "    userId VARCHAR(50) PRIMARY KEY,\n"
                    + "    password VARCHAR(50),\n"
                    + "    name VARCHAR(100),\n"
                    + "    email VARCHAR(100)\n"
                    + ");";

            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);

            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 사용자를 데이터베이스에 저장합니다.
     *
     * <p> 이미 등록된 아이디라면 오류를 발생시킵니다.
     *
     * @param user 사용자 객체
     * @throws IllegalArgumentException 이미 등록된 userId인 경우 발생
     */
    public static void addUser(User user) {

        String sql = "INSERT INTO Users (userId, password, name, email) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, user.getUserId());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getName());
            preparedStatement.setString(4, user.getEmail());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * 아이디와 일치하는 사용자를 조회합니다.
     *
     * @param userId 사용자 아이디
     * @return 조회한 사용자 정보
     * @throws NullPointerException 일치하는 사용자가 없는 경우 발생
     * @throws IllegalArgumentException sql 실행 실패시 발생
     */
    public static User findUserById(String userId) {

        String sql = "SELECT * FROM Users WHERE userId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String findUserId = resultSet.getString("userId");
                String findPwd = resultSet.getString("password");
                String findName = resultSet.getString("name");
                String findEmail = resultSet.getString("email");

                return new User(findUserId, findPwd, findName, findEmail);
            } else {
                throw new NullPointerException();
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * 모든 사용자를 조회합니다.
     *
     * @return 모든 사용자 리스트
     * @throws IllegalArgumentException sql 실행 실패시 발생
     */
    public static Collection<User> findAll() {

        Map<String, User> users = new HashMap<>();
        String sql = "SELECT * FROM Users";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String findUserId = resultSet.getString("userId");
                String findPwd = resultSet.getString("password");
                String findName = resultSet.getString("name");
                String findEmail = resultSet.getString("email");

                users.put(findUserId, new User(findUserId, findPwd, findName, findEmail));
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException();
        }

        return users.values();
    }
}
