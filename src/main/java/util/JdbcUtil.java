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

//    static {
//        try {
//            Class.forName("org.h2.Driver");
//            jdbcConnection = DriverManager.getConnection(JDBC_URL, H2_USER, H2_PASSWORD);
//        } catch (ClassNotFoundException | SQLException e) {
//            e.printStackTrace();
//        }
//    }

    public JdbcUtil() {}

    private static class LazyHolder {
        private static final Connection INSTANCE;
        static {
            try {
                Class.forName("org.h2.Driver");
                INSTANCE = DriverManager.getConnection(JDBC_URL, H2_USER, H2_PASSWORD);
            } catch (ClassNotFoundException | SQLException e) {
                throw new ExceptionInInitializerError(e);
            }
        }
    }

    public static Connection getJdbcConnection() {
        return LazyHolder.INSTANCE;
    }

    public static void close(Connection connection, Statement statement, ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
