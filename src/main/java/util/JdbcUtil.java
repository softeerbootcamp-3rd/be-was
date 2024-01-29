package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class JdbcUtil {

    private static final Logger logger = LoggerFactory.getLogger(JdbcUtil.class);

    private static final String JDBC_URL = "jdbc:h2:tcp://localhost:9092/../test";
    private static final String H2_USER = "sa";
    private static final String H2_PASSWORD = "";
    private static Connection jdbcConnection;

    private JdbcUtil() {}

    private static final ThreadLocal<Connection> connectionHolder = ThreadLocal.withInitial(() -> {
        try {
            Class.forName("org.h2.Driver");
            return DriverManager.getConnection(JDBC_URL, H2_USER, H2_PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            throw new ExceptionInInitializerError(e);
        }
    });

    public static Connection getJdbcConnection() {
        return connectionHolder.get();
    }

    public static void close() {
        try {
            if (jdbcConnection != null) {
                jdbcConnection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 추가: try-with-resources 적용
    public static void closeWithResources(Connection connection, Statement statement, ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
