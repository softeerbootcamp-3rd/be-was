package util.driver.jdbc;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CsvPreparedStatement implements PreparedStatement {

    private final String csvFilePath;

    private String sql;

    CsvPreparedStatement(String csvFilePath, String sql) {
        this.csvFilePath = csvFilePath;
        this.sql = sql;
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        // CSV 파일에서 데이터를 읽어와서 ResultSet 생성
        try {
            String tableName = extractTblName(sql);
            String filePath = csvFilePath + tableName;

            if (sql.contains("WHERE")) {
                // WHERE 문이 있는 경우 처리
                String[] parts = sql.split("WHERE");
                if (parts.length == 2) {
                    String whereCondition = parts[1].trim();
                    CsvResultSet resultSet = new CsvResultSet(new FileReader(filePath), whereCondition);
                    resultSet.applyFilter();
                    return resultSet;
                } else {
                    throw new SQLException("Invalid WHERE clause in the SQL query");
                }
            } else {
                // WHERE 문이 없는 경우 전체 데이터를 읽어옴
                CsvResultSet resultSet = new CsvResultSet(new FileReader(filePath));
                return resultSet;
            }
        } catch (IOException e) {
            throw new SQLException("Error reading CSV file", e);
        }
    }

    public static String extractTblName(String sql) {
        String pattern = "FROM\\s+(\\w+)\\s*"; // 정규 표현식 패턴

        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(sql);

        if (matcher.find()) {
            String tblName = matcher.group(1);
            return "/" + tblName + ".csv";
        } else {
            return "";
        }
    }

    @Override
    public int executeUpdate() throws SQLException {
        return 0;
    }

    @Override
    public void setNull(int parameterIndex, int sqlType) throws SQLException {

    }

    @Override
    public void setBoolean(int parameterIndex, boolean x) throws SQLException {

    }

    @Override
    public void setByte(int parameterIndex, byte x) throws SQLException {

    }

    @Override
    public void setShort(int parameterIndex, short x) throws SQLException {

    }

    @Override
    public void setInt(int parameterIndex, int x) throws SQLException {
        int index = 0;
        String[] sqls = sql.split(" ");
        String tmp = "";
        for (int i = 0; i < sqls.length; i++) {
            String[] subParts = sqls[i].split(" ");
            for (int j = 0; j < subParts.length; j++) {
                if ("?".equals(subParts[j])) {
                    index++;
                    if (parameterIndex == index) {
                        // ?를 x로 대체하고 특수 문자 처리
                        String replacedValue = Integer.toString(x).replace("\n", "\\n").replace(",", "\\,");
                        tmp += subParts[j] + replacedValue;
                    } else {
                        tmp += subParts[j];
                    }
                } else {
                    tmp += subParts[j];
                }
                // 마지막 요소인 경우 공백 추가하지 않음
                if (j < subParts.length - 1) {
                    tmp += " ";
                }
            }
            // 마지막 요소인 경우 공백 추가하지 않음
            if (i < sqls.length - 1) {
                tmp += " ";
            }
        }
        sql = tmp;
    }

    @Override
    public void setLong(int parameterIndex, long x) throws SQLException {

    }

    @Override
    public void setFloat(int parameterIndex, float x) throws SQLException {

    }

    @Override
    public void setDouble(int parameterIndex, double x) throws SQLException {

    }

    @Override
    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {

    }

    @Override
    public void setString(int parameterIndex, String x) throws SQLException {
        int index = 0;
        String[] sqls = sql.split(" ");
        String tmp = "";
        for (int i = 0; i < sqls.length; i++) {
            String[] subParts = sqls[i].split(" ");
            for (int j = 0; j < subParts.length; j++) {
                if ("?".equals(subParts[j]) || "?,".equals(subParts[j])) {
                    index++;
                    if (parameterIndex == index) {
                        // ?를 x로 대체하고 특수 문자 처리
                        String replacedValue = x.replace("\n", "\\n").replace(",", "\\,");
                        if (subParts[j].equals("?,")) {
                            tmp += replacedValue + ",";
                        } else {
                            tmp += replacedValue;
                        }
                    } else {
                        tmp += subParts[j];
                    }
                } else {
                    tmp += subParts[j];
                }
                // 마지막 요소인 경우 공백 추가하지 않음
                if (j < subParts.length - 1) {
                    tmp += " ";
                }
            }
            // 마지막 요소인 경우 공백 추가하지 않음
            if (i < sqls.length - 1) {
                tmp += " ";
            }
        }
        sql = tmp;
    }

    @Override
    public void setBytes(int parameterIndex, byte[] x) throws SQLException {

    }

    @Override
    public void setDate(int parameterIndex, Date x) throws SQLException {

    }

    @Override
    public void setTime(int parameterIndex, Time x) throws SQLException {

    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {

    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {

    }

    @Override
    public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {

    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {

    }

    @Override
    public void clearParameters() throws SQLException {

    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {

    }

    @Override
    public void setObject(int parameterIndex, Object x) throws SQLException {

    }

    @Override
    public boolean execute() throws SQLException {
        return false;
    }

    @Override
    public void addBatch() throws SQLException {

    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {

    }

    @Override
    public void setRef(int parameterIndex, Ref x) throws SQLException {

    }

    @Override
    public void setBlob(int parameterIndex, Blob x) throws SQLException {

    }

    @Override
    public void setClob(int parameterIndex, Clob x) throws SQLException {

    }

    @Override
    public void setArray(int parameterIndex, Array x) throws SQLException {

    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        return null;
    }

    @Override
    public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {

    }

    @Override
    public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {

    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {

    }

    @Override
    public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {

    }

    @Override
    public void setURL(int parameterIndex, URL x) throws SQLException {

    }

    @Override
    public ParameterMetaData getParameterMetaData() throws SQLException {
        return null;
    }

    @Override
    public void setRowId(int parameterIndex, RowId x) throws SQLException {

    }

    @Override
    public void setNString(int parameterIndex, String value) throws SQLException {

    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {

    }

    @Override
    public void setNClob(int parameterIndex, NClob value) throws SQLException {

    }

    @Override
    public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {

    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {

    }

    @Override
    public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {

    }

    @Override
    public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {

    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {

    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {

    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {

    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {

    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {

    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {

    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {

    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {

    }

    @Override
    public void setClob(int parameterIndex, Reader reader) throws SQLException {

    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {

    }

    @Override
    public void setNClob(int parameterIndex, Reader reader) throws SQLException {

    }

    @Override
    public ResultSet executeQuery(String sql) throws SQLException {
        return null;
    }

    @Override
    public int executeUpdate(String sql) throws SQLException {
        return 0;
    }

    @Override
    public void close() throws SQLException {

    }

    @Override
    public int getMaxFieldSize() throws SQLException {
        return 0;
    }

    @Override
    public void setMaxFieldSize(int max) throws SQLException {

    }

    @Override
    public int getMaxRows() throws SQLException {
        return 0;
    }

    @Override
    public void setMaxRows(int max) throws SQLException {

    }

    @Override
    public void setEscapeProcessing(boolean enable) throws SQLException {

    }

    @Override
    public int getQueryTimeout() throws SQLException {
        return 0;
    }

    @Override
    public void setQueryTimeout(int seconds) throws SQLException {

    }

    @Override
    public void cancel() throws SQLException {

    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return null;
    }

    @Override
    public void clearWarnings() throws SQLException {

    }

    @Override
    public void setCursorName(String name) throws SQLException {

    }

    @Override
    public boolean execute(String sql) throws SQLException {
        return false;
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        return null;
    }

    @Override
    public int getUpdateCount() throws SQLException {
        return 0;
    }

    @Override
    public boolean getMoreResults() throws SQLException {
        return false;
    }

    @Override
    public void setFetchDirection(int direction) throws SQLException {

    }

    @Override
    public int getFetchDirection() throws SQLException {
        return 0;
    }

    @Override
    public void setFetchSize(int rows) throws SQLException {

    }

    @Override
    public int getFetchSize() throws SQLException {
        return 0;
    }

    @Override
    public int getResultSetConcurrency() throws SQLException {
        return 0;
    }

    @Override
    public int getResultSetType() throws SQLException {
        return 0;
    }

    @Override
    public void addBatch(String sql) throws SQLException {

    }

    @Override
    public void clearBatch() throws SQLException {

    }

    @Override
    public int[] executeBatch() throws SQLException {
        return new int[0];
    }

    @Override
    public Connection getConnection() throws SQLException {
        return null;
    }

    @Override
    public boolean getMoreResults(int current) throws SQLException {
        return false;
    }

    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        return null;
    }

    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        return 0;
    }

    @Override
    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        return 0;
    }

    @Override
    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
        return 0;
    }

    @Override
    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        return false;
    }

    @Override
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        return false;
    }

    @Override
    public boolean execute(String sql, String[] columnNames) throws SQLException {
        return false;
    }

    @Override
    public int getResultSetHoldability() throws SQLException {
        return 0;
    }

    @Override
    public boolean isClosed() throws SQLException {
        return false;
    }

    @Override
    public void setPoolable(boolean poolable) throws SQLException {

    }

    @Override
    public boolean isPoolable() throws SQLException {
        return false;
    }

    @Override
    public void closeOnCompletion() throws SQLException {

    }

    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }
}
