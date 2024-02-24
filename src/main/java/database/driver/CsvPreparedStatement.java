package database.driver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class CsvPreparedStatement implements PreparedStatement {
    private static final Logger logger = LoggerFactory.getLogger(CsvPreparedStatement.class);

    private final String basePath;
    private final String[][] parsedQuery;
    private final String[][] replacedQuery;
    private final String tableFilePath;
    private final String[] columns;
    private Long lastId;

    private ResultSet resultSet;

    public CsvPreparedStatement(String basePath, String queryString) {
        this.basePath = basePath;
        String[] parts = CsvDriverUtils.split(queryString, ":");
        parsedQuery = new String[parts.length][];
        for (int i = 0; i < parsedQuery.length; i++) {
            parsedQuery[i] = CsvDriverUtils.split(parts[i], ",");
        }
        replacedQuery = CsvDriverUtils.arrayCopy(parsedQuery);
        tableFilePath = CsvDriverUtils.getTableFilePath(basePath, parsedQuery[1][0]);
        columns = readColumns();
        lastId = readLastId();

        resultSet = new CsvResultSet();
    }

    private String[] readColumns() {
        File file = new File(tableFilePath);
        if (!file.exists())
            return new String[0];

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return CsvDriverUtils.readRow(reader.readLine());
        } catch (IOException e) {
            return new String[0];
        }
    }

    private Long readLastId() {
        File file = new File(tableFilePath);
        if (!file.exists())
            return 0L;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line, previousLine = "";
            // 첫 줄 무시
            previousLine = reader.readLine();
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                previousLine = line;
            }
            return Long.parseLong(CsvDriverUtils.readRow(previousLine)[0]);
        } catch (Exception e) {
            return 0L;
        }
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        if ("select".equalsIgnoreCase(parsedQuery[0][0])) {
            List<String[]> result = selectRows();
            resultSet = new CsvResultSet(columns, result);
        }
        else if ("count".equalsIgnoreCase(parsedQuery[0][0])) {
            List<String[]> result = selectRows();
            String[] countColumn = new String[1];
            countColumn[0] = "count";

            List<String[]> countResult = new ArrayList<>();
            String[] count = new String[1];
            count[0] = Integer.toString(result.size());
            countResult.add(count);
            resultSet = new CsvResultSet(countColumn, countResult);
        }
        return resultSet;
    }

    private List<String[]> selectRows() {
        File file = new File(tableFilePath);
        if (!file.exists() || (parsedQuery.length != 2 && parsedQuery.length != 4))
            return new ArrayList<>();
        if (parsedQuery.length == 2)
            return readAll(file);
        return readFilter(file);
    }

    private List<String[]> readAll(File file) {
        List<String[]> resultList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            // 첫 줄 무시
            reader.readLine();
            while ((line = reader.readLine()) != null && !line.isEmpty())
                resultList.add(CsvDriverUtils.readRow(line));
        } catch (IOException ignored) {}
        return resultList;
    }

    private List<String[]> readFilter(File file) {
        List<String[]> resultList = new ArrayList<>();
        if (parsedQuery[2].length != parsedQuery[3].length)
            return resultList;

        String[] selectColumns = parsedQuery[2];
        String[] selectValues = replacedQuery[3];
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            // 첫 줄 무시
            reader.readLine();
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                boolean isSelected = true;
                String[] readValues = CsvDriverUtils.readRow(line);
                for (int i = 0; i < selectColumns.length; i++) {
                    for (int j = 0; j < columns.length; j++) {
                        if (Objects.equals(selectColumns[i], columns[j])
                                && !Objects.equals(selectValues[i], readValues[j])) {
                            isSelected = false;
                            break;
                        }
                    }
                }
                if (isSelected)
                    resultList.add(readValues);
            }
        } catch (IOException ignored) {}
        return resultList;
    }

    @Override
    public int executeUpdate() throws SQLException {
        if ("create".equalsIgnoreCase(parsedQuery[0][0]))
            return createTable();
        if ("insert".equalsIgnoreCase(parsedQuery[0][0]))
            return insertRow();
        if ("delete".equalsIgnoreCase(parsedQuery[0][0]))
            return deleteRow();
        return 0;
    }

    private synchronized int createTable() {
        if (parsedQuery.length < 3)
            return 0;
        String[] columns = parsedQuery[2];

        File folder = new File(basePath + "/");
        if (!folder.exists() && !folder.mkdirs()) {
            logger.error("cannot create folder");
        }

        File file = new File(tableFilePath);
        if (file.exists())
            return 0;
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.println(CsvDriverUtils.createRow(columns));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return 1;
    }

    private int insertRow() {
        if (parsedQuery.length < 4 || parsedQuery[2].length != parsedQuery[3].length)
            return 0;
        String[] insertColumns = parsedQuery[2];
        String[] insertValues = replacedQuery[3];

        try (RandomAccessFile file = new RandomAccessFile(tableFilePath, "rw");
             FileChannel channel = file.getChannel()) {
            String[] matchedValues = new String[columns.length];
            if ("id".equals(columns[0])) {
                lastId += 1;
                matchedValues[0] = "" + lastId;
            }
            for (int i = 0; i < insertColumns.length; i++) {
                // 일치하는 column name 찾기
                for (int j = 0; j < columns.length; j++) {
                    if (Objects.equals(insertColumns[i], columns[j])) {
                        matchedValues[j] = insertValues[i];
                        break;
                    }
                }
            }
            FileLock lock = channel.lock();
            file.seek(file.length());
            file.write((CsvDriverUtils.createRow(matchedValues)+ "\n").getBytes("UTF-8"));
            lock.release();

            List<String[]> generatedValue = new ArrayList<>();
            generatedValue.add(matchedValues);
            resultSet = new CsvResultSet(columns, generatedValue);
            return 1;
        } catch (Exception e) {
            return -1;
        }
    }

    private synchronized int deleteRow() {
        try (BufferedReader reader = new BufferedReader(new FileReader(tableFilePath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tableFilePath + ".temp"))) {
            String[] selectColumns = parsedQuery[2];
            String[] selectValues = replacedQuery[3];

            String line;
            line = reader.readLine();
            writer.write(line);
            writer.newLine();
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                boolean isSelected = true;
                String[] readValues = CsvDriverUtils.readRow(line);
                for (int i = 0; i < selectColumns.length; i++) {
                    for (int j = 0; j < columns.length; j++) {
                        if (Objects.equals(selectColumns[i], columns[j])
                                && !Objects.equals(selectValues[i], readValues[j])) {
                            isSelected = false;
                            break;
                        }
                    }
                }
                if (!isSelected) {
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
            return -1;
        }
        File originalFile = new File(tableFilePath);
        File modifiedFile = new File(tableFilePath + ".temp");
        if (modifiedFile.renameTo(originalFile)) {
            return 1;
        } else {
            logger.error("Failed to modify the file.");
            return -1;
        }
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

    }

    @Override
    public void setLong(int parameterIndex, long x) throws SQLException {
        setString(parameterIndex, Long.toString(x));
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
        for (int i = 0; i < parsedQuery.length; i++) {
            for (int j = 0; j < parsedQuery[i].length; j++) {
                if ("?".equals(parsedQuery[i][j])) {
                    index++;
                    if (parameterIndex == index) {
                        replacedQuery[i][j] = x.replace("\n", "\\n")
                                .replace(",", "\\,");
                        return;
                    }
                }
            }
        }
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
        setString(parameterIndex, "" + x.getTime());
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
        return ResultSet.FETCH_FORWARD;
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

        if ("create".equalsIgnoreCase(parsedQuery[0][0])
                || "insert".equalsIgnoreCase(parsedQuery[0][0])
                || "delete".equalsIgnoreCase(parsedQuery[0][0]))
            return ResultSet.CONCUR_UPDATABLE;
        return ResultSet.CONCUR_READ_ONLY;
    }

    @Override
    public int getResultSetType() throws SQLException {
        return ResultSet.TYPE_FORWARD_ONLY;
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
        return resultSet;
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
