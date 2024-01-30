package database.driver;

import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

public class CsvJdbcDriver implements Driver {
    private static final String acceptUrl = "jdbc:csv:";

    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        String filePath = url.substring(acceptUrl.length());
        return new CsvConnection(filePath);
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        return url.startsWith(acceptUrl);
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        return new DriverPropertyInfo[0];
    }

    @Override
    public int getMajorVersion() {
        return 1;
    }

    @Override
    public int getMinorVersion() {
        return 0;
    }

    @Override
    public boolean jdbcCompliant() {
        return false;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    }
}
