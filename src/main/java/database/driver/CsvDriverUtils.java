package database.driver;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class CsvDriverUtils {

    public static String[] split(String str, String delimiter) {
        String[] parts = str.split(delimiter, -1);
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
            sb.append(encodeToBase64(values[i]));
            if (i < values.length - 1)
                sb.append(",");
        }
        return sb.toString();
    }

    public static String[] readRow(String line) {
        String[] result = split(line, ",");
        for (int i = 0; i < result.length; i++) {
            result[i] = decodeBase64(result[i]);
        }
        return result;
    }

    private static String encodeToBase64(String input) {
        try {
            byte[] inputBytes = input.getBytes("UTF-8");
            byte[] encodedBytes = Base64.getEncoder().encode(inputBytes);
            return new String(encodedBytes, "UTF-8");
        } catch (UnsupportedEncodingException ignored) {
            return "";
        }
    }

    private static String decodeBase64(String encodedString) {
        try {
            byte[] encodedBytes = encodedString.getBytes("UTF-8");
            byte[] decodedBytes = Base64.getDecoder().decode(encodedBytes);
            return new String(decodedBytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return encodedString;
        }
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
