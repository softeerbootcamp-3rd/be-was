package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class JdbcUtil {

    private static final Logger logger = LoggerFactory.getLogger(JdbcUtil.class);

    private static final String JDBC_URL = "jdbc:h2:tcp://localhost:9092/../test";
    private static final String H2_USER = "sa";
    private static final String H2_PASSWORD = "";

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

    public static void closeJdbcConnection() {
        Connection connection = connectionHolder.get();
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            logger.error("JDBC 커넥션 닫기 오류", e);
        } finally {
            connectionHolder.remove(); // ThreadLocal에서 현재 스레드에 연결된 Connection 제거
        }
    }

}
