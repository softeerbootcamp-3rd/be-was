package database.driver;

public class CsvDriverUtils {

    public static String[] split(String str, String delimiter) {
        String[] parts = str.split(delimiter + "(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim().replaceAll("^\"|\"$", "");
        }
        return parts;
    }

    public static String getTableFilePath(String basePath, String tableName) {
        return basePath + "/" + tableName + ".csv";
    }

    public static String createRow(String[] values) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            if (values[i] == null)
                values[i] = "";
            sb.append(values[i]);
            if (i < values.length - 1)
                sb.append(",");
        }
        return sb.toString();
    }

    public static String[][] arrayCopy(String[][] array) {
        String[][] result = new String[array.length][];
        for (int i = 0; i < result.length; i++) {
            result[i] = new String[array[i].length];
            for (int j = 0; j < result[i].length; j++)
                result[i][j] = array[i][j];
        }
        return result;
    }
}
