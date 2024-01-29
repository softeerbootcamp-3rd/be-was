package database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class H2Database {
    private static final Logger logger = LoggerFactory.getLogger(H2Database.class);

    private static final String jdbcUrl = "jdbc:h2:./data/be_was";
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
                "CREATE TABLE IF NOT EXISTS users " +
                        "(userId VARCHAR(255) PRIMARY KEY, password VARCHAR(255), " +
                        "name VARCHAR(255), email VARCHAR(255))");
             PreparedStatement postStatement = connection.prepareStatement(
                     "CREATE TABLE IF NOT EXISTS posts " +
                             "(id BIGINT PRIMARY KEY AUTO_INCREMENT, writerId VARCHAR(255), " +
                             "title VARCHAR(255), contents VARCHAR(1000), createDatetime TIMESTAMP)");
             PreparedStatement commentStatement = connection.prepareStatement(
                     "CREATE TABLE IF NOT EXISTS comments " +
                             "(id BIGINT PRIMARY KEY AUTO_INCREMENT, postId BIGINT, " +
                             "writerId VARCHAR(255), contents VARCHAR(255), createDatetime TIMESTAMP)");
             PreparedStatement attachmentStatement = connection.prepareStatement(
                     "CREATE TABLE IF NOT EXISTS attachments " +
                             "(id BIGINT PRIMARY KEY AUTO_INCREMENT, postId BIGINT, " +
                             "filename VARCHAR(255), mimeType VARCHAR(255), data BLOB)"
             )
        ) {
            usersStatement.executeUpdate();
            postStatement.executeUpdate();
            commentStatement.executeUpdate();
            attachmentStatement.executeUpdate();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(jdbcUrl, user, password);
    }
}