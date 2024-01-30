package database.driver;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final String jdbcUrl = "jdbc:csv:data/be_was";
    private static final String user = "sa";
    private static final String password = "";

    static {
        CsvJdbcDriver driver = new CsvJdbcDriver();
        try (Connection connection = DriverManager.getConnection(jdbcUrl, user, password)) {
            initializeDatabase(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Main() throws SQLException {
    }

    private static void initializeDatabase(Connection connection) throws SQLException {
        try (PreparedStatement usersStatement = connection.prepareStatement(
                "create: users: userId, password, name, email");
             PreparedStatement boardStatement = connection.prepareStatement(
                     "create: boards: id, writerId, title, contents, createDatetime");
             PreparedStatement commentStatement = connection.prepareStatement(
                     "create: comments: id, boardId, writerId, contents, createDatetime");
             PreparedStatement attachmentStatement = connection.prepareStatement(
                     "create: attachments: id, boardId, filename, mimeType, savedPath")
        ) {
            usersStatement.executeUpdate();
            boardStatement.executeUpdate();
            commentStatement.executeUpdate();
            attachmentStatement.executeUpdate();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(jdbcUrl, user, password);
    }

    public static void main(String[] args) {
        String query = "select: boards: writerId: ?";

        List<Long> comments = new ArrayList<>();

        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            statement.setString(1, "asdf");
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    comments.add(resultSet.getLong(1));
                    System.out.println(resultSet.getLong(1));
                }
            }
        } catch (SQLException e) {
            return;
        }
        System.out.println("done");

        return;
    }
}
