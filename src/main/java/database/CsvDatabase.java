package database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class CsvDatabase {
    private static final Logger logger = LoggerFactory.getLogger(CsvDatabase.class);

    private static final String jdbcUrl = "jdbc:csv:data/be_was";
    private static final String user = "sa";
    private static final String password = "";

    static {
        try (Connection connection = DriverManager.getConnection(jdbcUrl, user, password)) {
            initializeDatabase(connection);
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    private static void initializeDatabase(Connection connection) throws SQLException {
        try (PreparedStatement usersStatement = connection.prepareStatement(
                "create: users: userId, password, name, email");
             PreparedStatement boardStatement = connection.prepareStatement(
                     "create: boards: id, writerId, title, contents, createDatetime");
             PreparedStatement commentStatement = connection.prepareStatement(
                     "create: comments: id, boardId, writerId, contents, createDatetime");
             PreparedStatement attachmentStatement = connection.prepareStatement(
                     "create: attachments: id, boardId, filename, mineType, savedPath")
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
}